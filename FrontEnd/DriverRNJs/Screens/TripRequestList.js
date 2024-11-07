import React, { useEffect, useState } from "react";
import { View, Text, FlatList, TouchableOpacity, Modal } from "react-native";
import WebSocketService from "../Components/WebSocketService";
import { Button, Provider as PaperProvider } from "react-native-paper";
import tw from "twrnc";
import { useDispatch, useSelector } from "react-redux";
import { selectUserInfo, selectTripreqList } from "../Slices/navSlice";

const TripRequestList = () => {
  const [selectedItem, setSelectedItem] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);

  const userInfo = useSelector(selectUserInfo);
  const tripReqList = useSelector(selectTripreqList)|| [];
  const dispatch = useDispatch();

  const [passReqListdata, setPassReqListdata] = useState([]);

  const handleItemPress = (item) => {
    setSelectedItem(item);
    setIsModalVisible(true);
  };

  const closeModal = () => {
    setIsModalVisible(false);
    setSelectedItem(null);
  };

  useEffect(() => {
    // Connect to the WebSocket with userInfo from Redux
    WebSocketService.connect(userInfo, dispatch);

    return () => {
      WebSocketService.disconnect();
    };
  }, []);

  useEffect(() => {
    

    const updateList = tripReqList.map((item, index) => ({
      ...item,
      id: index + 1,
    }));

    setPassReqListdata(updateList);
    console.log(passReqListdata);
  }, [tripReqList]);

  if (!passReqListdata || passReqListdata.length === 0) {
    return (
      <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
        <Text style={tw`text-2xl font-bold mb-5 text-center`}>
          Taxi Ride List
        </Text>
        <Text>No trip requests available.</Text>
      </View>
    );
  }

  return (
    <PaperProvider>
      <View style={tw`flex-1 pt-12 px-5`}>
        <Text style={tw`text-2xl font-bold mb-5 text-center`}>Your Trip List</Text>

        <FlatList
          data={passReqListdata}
          keyExtractor={(item) => item.id}
          renderItem={({ item }) => (
            <TouchableOpacity
              style={tw`p-4 bg-gray-100 border-b border-gray-300 rounded-lg mb-2`}
              onPress={() => handleItemPress(item)}
            >
              <Text style={tw`text-lg`}> To: {item.parsedMessage.destinationName} </Text>
              <Text style={tw`text-lg`}> Price:{item.parsedMessage.startLon} </Text>
            </TouchableOpacity>
          )}
        />

        <Modal
          transparent={true}
          visible={isModalVisible}
          animationType="slide"
          onRequestClose={closeModal}
        >
          <View
            style={tw`flex-1 justify-center items-center bg-black bg-opacity-50`}
          >
            <View style={tw`w-72 p-5 bg-white rounded-lg items-center`}>
              {selectedItem && (
                <>
                  <Text style={tw`text-xl font-bold mb-5`}>
                    Ride Distance: {selectedItem.parsedMessage.startLon}
                  </Text>
                  <Text style={tw`text-base mb-1 text-center`}>
                    To: {selectedItem.parsedMessage.destinationName}
                  </Text>
                  <Text style={tw`text-base mb-1 text-center`}>
                    Your Cut:  {selectedItem.parsedMessage.startLon}
                  </Text>
                  <Text style={tw`text-base mb-1 text-center`}>
                    Phone: {selectedItem.parsedMessage.startLon}
                  </Text>
                  <Text style={tw`text-xl mt-4 text-center`}>
                   Ride this Taxi
                  </Text>

                  <View style={tw`flex-row justify-center  w-full mt-2 mb-4`}>
                    <Button
                      mode="contained"
                      onPress={() => console.log(joinList)}
                      buttonColor="#FFD700" // Set a yellow color
                      textColor="black" 
                    >
                      Confirm
                    </Button>
                  </View>

                  <Button onPress={closeModal}  textColor="black" >Close</Button>
                </>
              )}
            </View>
          </View>
        </Modal>
      </View>
    </PaperProvider>
  );
};

export default TripRequestList;
