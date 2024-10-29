import React from "react";
import {
  FlatList,
  Image,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  Alert,
} from "react-native";

import SingleRideImage from "../assets/SingleRide.png";
import LongRideImage from "../assets/LongRide.jpeg";
import JoinRideImage from "../assets/joinRide.png";
import tw from "twrnc";
import { Icon } from "react-native-elements";
import { useNavigation } from "@react-navigation/native";
import { useSelector } from "react-redux";
import { selectOrigin, setRideType } from "../slices/navSlice";

import { useDispatch } from "react-redux";
const data = [
  {
    id: "123",
    title: "Get a Ride",
    image: SingleRideImage,
    screen: "ToMapScreen",
    ridetype: "1",
  },
  {
    id: "456",
    title: "Join Another",
    image: JoinRideImage,
    screen: "ToMapScreen",
    ridetype: "2",
  },
  {
    id: "457",
    title: "Far journey",
    image: LongRideImage,
    screen: "DistantRoute",
    ridetype: "3",
    hint:"*Journey over 10km"
                  
  },
  {
    id: "458",
    title: "Schedule Join Taxi",
    image: LongRideImage,
    screen: "ToMapScreen",
    ridetype: "4",
  },
];

const NavOptions = () => {
  const navigation = useNavigation();
  const origin = useSelector(selectOrigin);
  const dispatch = useDispatch();

  return (
    <FlatList
      data={data}
      keyExtractor={(item) => item.id}
      numColumns={2}
      renderItem={({ item }) => (
        <TouchableOpacity
          onPress={() => {
            if (!origin) {
              Alert.alert("No Location Selected.");
            } else {
              navigation.navigate(item.screen);
              dispatch(
                setRideType({
                  ridetype: item.ridetype,
                })
              );
            }
          }}
          style={tw`m-2 w-40 pl-6 p-2`}
        >
          <View>
            <Image
              style={{ width: 120, height: 120, resizeMode: "contain" }}
              source={item.image}
            />
          </View>
          <Text style={tw`mt-2 text-lg font-semibold`}>{item.title}</Text>
          {item.hint ? (
  <Text style={tw`text-base mb-1 text-gray-500`}>{item.hint}</Text>
) : null}
          <Icon
            style={tw`p-2 bg-black rounded-full w-10 mt-4`}
            name="arrowright"
            color="white"
            type="antdesign"
          />
        </TouchableOpacity>
      )}
    />
  );
};

export default NavOptions;
