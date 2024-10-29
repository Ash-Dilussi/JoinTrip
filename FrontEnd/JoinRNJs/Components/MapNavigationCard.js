import React from "react";
import {
  Alert,
  FlatList,
  Image,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { GooglePlacesAutocomplete } from "react-native-google-places-autocomplete";
import { useDispatch, useSelector } from "react-redux";
import tw from "twrnc";
import {
  selectRideType,
  setDestination,
  selectDestination,
  selectSegmentDistanceKm,
  selectTripDistanceKm
} from "../slices/navSlice";
import { GOOGLE_MAPS_APIKEY } from "@env";
import { useNavigation } from "@react-navigation/native";

import { Icon } from "react-native-elements";

const MapNavigationCard = () => {
  const dispatch = useDispatch();
  const navigation = useNavigation();
  const rideType = useSelector(selectRideType);
  const destination = useSelector(selectDestination);
  const segmentDistancekm = useSelector(selectSegmentDistanceKm);
  const tripdistance = useSelector(selectTripDistanceKm);

  const rtype = rideType.ridetype;

  const handlePress = () => {
 
    if (!destination) {
      Alert.alert("No Destination Selected.");
      return;
    } else {
      if (rtype == 4) {
        navigation.navigate("ScheduleTimeCard");
      } else if(rtype == 3 && (tripdistance.distance/1000) < segmentDistancekm){
        Alert.alert("Your trip is Less than expected for this Service. Please Select distance of Greater that 10km.");
        return;
      }else {
        navigation.navigate("SelectOptionCard");
      }
    }
  };
 
  return (
    <View>
      <Text style={tw`text-center py-5 text-xl`}>Good Day</Text>
      <View>
        <GooglePlacesAutocomplete
          styles={{
            container: { flex: 0 },
            textInput: { fontSize: 18, backgroundColor: "#F5F5F5" },
            textInputContainer: { paddingHorizontal: 20, paddingBottom: 1 },
          }}
          fetchDetails={true}
          returnKeyType={"search"}
          query={{
            key: GOOGLE_MAPS_APIKEY,
            language: "en",
          }}
          onPress={(data, details = null) => {
            dispatch(
              setDestination({
                location: details.geometry.location,
                description: data.description,
              })
            );
          }}
          placeholder="Where to?"
          debounce={400}
        />
      </View>

      <View>
        <TouchableOpacity style={tw`mt-0 mx-0`} onPress={handlePress}>
          <Icon
            style={tw`p-1 bg-blue-500  rounded-full w-10 ml-80 mt-1`}
            name="arrowright"
            color="white"
            type="antdesign"
          />
        </TouchableOpacity>
      </View>
    </View>
  );
};

export default MapNavigationCard;
