import React, { useEffect, useState, useRef } from "react";
import { Alert, Text, TouchableOpacity, PermissionsAndroid, View } from "react-native";
import { Icon } from "react-native-elements";

import tw from "twrnc";
import MapNavigationCard from "../Components/MapNavigationCard";
import MapView, { Polyline, Marker } from "react-native-maps";
import SelectOptionCard from "../Components/SelectOptionCard";
import { createStackNavigator } from "@react-navigation/stack";
import { useSelector } from "react-redux";
import {
  selectDestination,
  selectOrigin,
  setFarRoute, 
  setTripDistanceKm
} from "../slices/navSlice";
import { GOOGLE_MAPS_APIKEY } from "@env";
import { useDispatch } from "react-redux";
import axios from "axios";

const DistantRoute = () => {
  const Stack = createStackNavigator();
  const mapRef = useRef(null);
  const [route, setRoute] = useState([]); 
  const origin = useSelector(selectOrigin);
  const destination = useSelector(selectDestination);
  
  const dispatch = useDispatch();

  const currentLocation = {
    latitude: origin.location.lat,
    longitude: origin.location.lng,
  };

  useEffect(() => {
    (async () => {

      if (!origin || !destination) return;
      mapRef.current.fitToSuppliedMarkers(["origin", "destination"], {
        edgePadding: { top: 50, righ: 50, bottom: 50, left: 50 },
      });

      if (destination) {
        getRoute(
          origin.location.lat,
          origin.location.lng,
          destination.location.lat,
          destination.location.lng
        );
      }
    })();
  }, [origin, destination]);

 
  const getRoute = async (pickupLat, pickupLng, destLat, destLng) => {
    try {
      //console.log( "new " + origin.location.lat + "   " + origin.location.lng + "   destinaiton:=>  " + destination.location.lat);
      
      const response = await axios.get(
        `https://maps.googleapis.com/maps/api/directions/json?origin=${pickupLat},${pickupLng}&destination=${destLat},${destLng}&key=${GOOGLE_MAPS_APIKEY}`
      );

      if (response.data.routes.length > 0) {
        const tripdistance = response.data.routes[0].legs[0].distance; 
        dispatch(setTripDistanceKm({
          distance: tripdistance.value,
          text: tripdistance.text
        }))
    
       // console.log('Distance in meters:', distance.value);
        
      } else {
        console.log('No route found');
      }
      //console.log(response)
      const points = decodePolyline(
        response.data.routes[0].overview_polyline.points
      );
      setRoute(points);
      dispatch(
        setFarRoute({
          route: points,
        })
      );
    } catch (error) {
      console.error(error);
    }
  };

  // decode the polyline points (Google Maps API returns encoded polyline)
  const decodePolyline = (t, e = 5) => {
    let points = [];
    let lat = 0,
      lng = 0;
    for (let index = 0; index < t.length; ) {
      let b,
        shift = 0,
        result = 0;
      do {
        b = t.charCodeAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);
      const dlat = result & 1 ? ~(result >> 1) : result >> 1;
      lat += dlat;

      shift = result = 0;
      do {
        b = t.charCodeAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);
      const dlng = result & 1 ? ~(result >> 1) : result >> 1;
      lng += dlng;

      points.push({
        latitude: lat / Math.pow(10, e),
        longitude: lng / Math.pow(10, e),
      });
    }
    return points;
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
    <View>
      <View style={tw` h-1/2`}>
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
          {/* Polyline for the Route */}
          {route.length > 0 && (
            <Polyline coordinates={route} strokeWidth={5} strokeColor="blue" />
          )}
        </MapView>
        <TouchableOpacity
            //style={tw`mt-0 mx-0`}
            style={tw`absolute bottom-5 right-5 p-3`}
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
      <View >
          
        </View>

      <View style={tw`h-1/2`}>
        <Stack.Navigator>
          <Stack.Screen
            name="MapNavigationCard"
            component={MapNavigationCard}
            options={{
              headerShown: false,
            }}
          />
          <Stack.Screen
            name="SelectOptionCard"
            component={SelectOptionCard}
            options={{
              headerShown: false,
            }}
          />
        </Stack.Navigator>
      </View>
    </View>
  );
};

export default DistantRoute;
