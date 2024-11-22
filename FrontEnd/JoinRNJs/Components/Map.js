import React, { useState, useEffect, useRef } from "react";
import { View, Button, Image, TouchableOpacity } from "react-native";
import { Icon } from "react-native-elements";
import tw from "twrnc";
import MapView, { Marker } from "react-native-maps";
import {
  selectDestination,
  selectTaxiLocation,
  selectOrigin,
} from "../slices/navSlice";
import { useSelector } from "react-redux";
import MapViewDirections from "react-native-maps-directions";
import { GOOGLE_MAPS_APIKEY } from "@env";
//import { ClusteredMapView } from 'react-native-maps-super-cluster';

const Map = () => {
  const origin = useSelector(selectOrigin);
  const destination = useSelector(selectDestination);
  const mapRef = useRef(null);
  const [taxiRiderListData, setTaxiRiderListData] = useState([]);

  const currentLocation = {
    latitude: origin.location.lat,
    longitude: origin.location.lng,
  };
  const [currentRegion, setCurrentRegion] = useState({
    latitude: origin.location.lat,
    longitude: origin.location.lng,
    latitudeDelta: 0.005,
    longitudeDelta: 0.005,
  });

  const taxiLocation = useSelector(selectTaxiLocation);

  useEffect(() => {
    setTaxiRiderListData([])
    const updatedTaxiData = taxiLocation.map((item, index) => ({
        ...item,
        id: index+1,
      }));
      setTaxiRiderListData(updatedTaxiData);
    console.log(taxiRiderListData);
  }, [taxiLocation]);

  useEffect(() => {
    if (!origin || !destination) return;
    mapRef.current.fitToSuppliedMarkers(["origin", "destination"], {
      edgePadding: { top: 50, righ: 50, bottom: 50, left: 50 },
    });
  }, [origin, destination, taxiRiderListData]);

  const zoomOut = () => {
    setCurrentRegion({
      ...currentRegion,
      latitudeDelta: currentRegion.latitudeDelta * 10,
      longitudeDelta: currentRegion.longitudeDelta * 10,
    });
  };

  const goToCurrentLocation = () => {
    if (mapRef.current) {
      mapRef.current.animateToRegion(
        {
          ...currentLocation,
          latitudeDelta: 0.005, // Adjust for zoom level
          longitudeDelta: 0.005,
        },
        1000
      );
    }
  };

  return (
    <View style={tw`flex-1`}>
      <MapView
        ref={mapRef}
        style={tw`flex-1`}
        mapType="mutedStandard"
        initialRegion={{
          latitude: origin.location.lat,
          longitude: origin.location.lng,
          latitudeDelta: 0.005,
          longitudeDelta: 0.005,
        }}
      >
        {origin && destination && (
          <MapViewDirections
            origin={origin.description}
            destination={destination.description}
            apikey={GOOGLE_MAPS_APIKEY}
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
            description={destination.description}
            identifier="destination"
          />
        )}

         {
          //taxiRiderListdata.length > 0 &&
          taxiRiderListData.map((taxi) => (
            <Marker
            key={taxi.id}
              coordinate={{
                latitude:taxi.lat,
                longitude: taxi.lng,
              }}
              title="Taxi Tider"
              description="Taxi"
              identifier="taxi"
              // image={require("../assets/taxiWheel.jpg")} // Custom image for marker icon
            >
              <Image
                source={require("../assets/taxiWheel.jpg")}
                style={[
                  tw`w-10 h-10`, // Adjust size as needed
                  { borderRadius: 20, borderWidth: 0.5, borderColor: "white" },
                ]}
                resizeMode="cover"
              />
            </Marker>
          ))
        } 

       
      </MapView>

      {/* Zoom Out Button */}
      {/* <TouchableOpacity
        style={tw`absolute top-5 left-5 p-3 bg-gray-800 rounded-full`}
        onPress={zoomOut}
      >
        <Button title="Zoom Out" color="white" onPress={zoomOut} />
      </TouchableOpacity> */}

      {/* Current Location Button */}
      <TouchableOpacity
        style={tw`absolute bottom-0 right-5 p-3  `}
        onPress={goToCurrentLocation}
      >
        <Icon
          style={tw`p-.6 bg-blue-500  rounded-full w-10 ml-80 mt-1`}
          name="arrowup"
          color="white"
          type="antdesign"
        />
      </TouchableOpacity>
    </View>
  );
};

export default Map;
