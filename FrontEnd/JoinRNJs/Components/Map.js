import React, { useEffect, useRef } from 'react'
import { FlatList, Image, SafeAreaView, StyleSheet, Text, TouchableOpacity, View } from 'react-native'

import tw from 'twrnc';
import MapView, { Marker } from 'react-native-maps';
import { selectDestination, selectOrigin } from '../slices/navSlice';
import { useSelector } from 'react-redux';
import MapViewDirections from 'react-native-maps-directions';
import { GOOGLE_MAPS_APIKEY } from "@env";
import { useFocusEffect } from '@react-navigation/native';



const Map = () => {

    const origin = useSelector(selectOrigin);
    const destination= useSelector(selectDestination);
    const mapRef = useRef(null);
    const destest = useSelector(selectDestination);

    useEffect(() =>{
        if(!origin || !destination) return;
        console.log(destest);
        mapRef.current.fitToSuppliedMarkers(["origin","destination"],
            {
                edgePadding: {top:50, righ:50, bottom:50, left: 50}
            }
        );
    }, [origin,destination]);

    

    return (
        <MapView
            ref= {mapRef}
            style={tw`flex-1`}
            mapType='mutedStandard'
            initialRegion={{
                latitude: origin.location.lat,
                longitude: origin.location.lng,
                latitudeDelta: 0.005,
                longitudeDelta: 0.005,
            }}
        >

            {origin && destination &&(
                <MapViewDirections
                origin={origin.description}
                destination = {destination.description}
                apikey= {GOOGLE_MAPS_APIKEY}
                strokeWidth={3}
                strokeColor="black"

                />
            )}

            {origin?.location && (
                <Marker
                    coordinate={{
                        latitude: origin.location.lat,
                        longitude: origin.location.lng,
                    }}
                    title="Origin"
                    description={origin.description}
                    identifier="origin"
                />
            )}

            {destination?.location && (
                <Marker
                    coordinate={{
                        latitude: destination.location.lat,
                        longitude: destination.location.lng,
                    }}
                    title="Destination"
                    description={origin.description}
                    identifier="destination"
                />
            )}
            
        </MapView>
    )
}

export default Map

const styles = StyleSheet.create({})