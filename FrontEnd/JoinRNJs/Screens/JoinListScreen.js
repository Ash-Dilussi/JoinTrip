import React, { useEffect, useState, useCallback } from "react";
import { View, Text, FlatList, TouchableOpacity, Modal } from "react-native";
import { Button, Provider as PaperProvider } from "react-native-paper";
import { useSelector } from "react-redux";
import {
  selectCurrentJoinReqId,
  selectJoinList,
  selectUserInfo,
} from "../slices/navSlice";
import tw from "twrnc";
import WebSocketService from "../Components/WebSocketService";
import { useFocusEffect } from "@react-navigation/native";
import axios from "axios";
import { API_BASE_URL } from "@env";

const JoinListScreen = () => {
  const [selectedItem, setSelectedItem] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);


  const joinList = useSelector(selectJoinList);
  const userInfo = useSelector(selectUserInfo);
  const joinReqId = useSelector(selectCurrentJoinReqId);

  const joinListdata = [];
  useEffect(() => {
    // Connect to the WebSocket with userInfo from Redux
    WebSocketService.connect(userInfo);

    return () => {
      WebSocketService.disconnect();
    };
  }, []);

  useFocusEffect(
    useCallback(() => {
      joinList.forEach((item) => {
        joinListdata.push(item);
      });

      return () => {};
    }, [isModalVisible])
  );

  const HandleConfirm = async(recUId,recJReqId) => {
    try {
      
      const response = await axios.post(
        `${API_BASE_URL}/passenger/msgJoinControl`,
        {
          senderUserId: userInfo.primaryUserInfo.userid,
          senderJoinReqId: joinReqId,
          receiverUserId: recUId,
          receiverJoinReqId: recJReqId,
          reqStatus: 2,
        }
      );
      console.log(joinReqId)
    } catch (error) {
      console.error("Error sending route to backend:", error);
    }
  };

  const handleItemPress = (item) => {
    setSelectedItem(item);
    setIsModalVisible(true);
  };

  const closeModal = () => {
    setIsModalVisible(false);
    setSelectedItem(null);
  };

  return (
    <PaperProvider>
      <View style={tw`flex-1 pt-12 px-5`}>
        <Text style={tw`text-2xl font-bold mb-5 text-center`}>Join List</Text>

        <FlatList
          data={joinListdata}
          keyExtractor={(item) => item.userid}
          renderItem={({ item }) => (
            <TouchableOpacity
              style={tw`p-4 bg-gray-100 border-b border-gray-300 rounded-lg mb-2`}
              onPress={() => handleItemPress(item)}
            >
              <Text style={tw`text-lg`}>
                {item.firstName} {item.lastName}
              </Text>
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
                    {selectedItem.firstName} {selectedItem.lastName}
                  </Text>
                  <Text style={tw`text-base mb-1 text-center`}>
                    {selectedItem.town}
                  </Text>
                  <Text style={tw`text-base mb-1 text-center`}>
                    {selectedItem.gender}
                  </Text>
                  <Text style={tw`text-base mb-1 text-center`}>
                    {selectedItem.phone}
                  </Text>
                  <Text style={tw`text-base mb-1 text-center`}>
                    I’m all in for where you're headed!
                  </Text>

                  <View style={tw`flex-row justify-center  w-full mt-5 mb-4`}>
                    <Button
                      mode="contained"
                      onPress={() =>HandleConfirm(selectedItem.userid,selectedItem.joinReqId)}
                    >
                      Join Me!
                    </Button>
                  </View>

                  <Button onPress={closeModal}>Close</Button>
                </>
              )}
            </View>
          </View>
        </Modal>
      </View>
    </PaperProvider>
  );
};

export default JoinListScreen;
