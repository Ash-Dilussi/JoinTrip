import React, { useEffect, useState, useCallback } from "react";
import {
  Alert,
  View,
  Text,
  FlatList,
  TouchableOpacity,
  Modal,
} from "react-native";
import { Button, Provider as PaperProvider } from "react-native-paper";
import { useSelector, useDispatch } from "react-redux";
import {
  selectCurrentJoinReqId,
  selectJoinList,
  selectNewJoinRequest,
  selectUserInfo,
  replaceJoinListItem,
  setNewJoinRequest,
} from "../slices/navSlice";
import tw from "twrnc";
import WebSocketService from "../Components/WebSocketService";
import { useFocusEffect } from "@react-navigation/native";
import axios from "axios";
import { API_BASE_URL } from "@env";

const JoinListScreen = ({ navigation }) => {
  const [selectedItem, setSelectedItem] = useState(null);
  const [isModal01Visible, setisModal01Visible] = useState(false);
  const [isModal02Visible, setisModal02Visible] = useState(false);

  const joinList = useSelector(selectJoinList);
  const userInfo = useSelector(selectUserInfo);
  const joinReqId = useSelector(selectCurrentJoinReqId);
  const newJoinReq = useSelector(selectNewJoinRequest);
  const dispatch = useDispatch();

  const joinListdata = [];
  useEffect(() => {
    // Connect to the WebSocket with userInfo from Redux
    WebSocketService.connect(userInfo, dispatch);

    return () => {
      WebSocketService.disconnect();
    };
  }, []);

  useEffect(() => {
    if (newJoinReq != null && joinList.length > 0) {
      const newReq = joinList.find(
        (item) => item.joinReqId === newJoinReq.senderJoinReqId
      );
      console.log("in useeff", newJoinReq);
      if (newJoinReq.reqStatus === "Reqeust") {
        console.log(" in the iff", newReq);
        setSelectedItem(newReq);
        setisModal02Visible(true);
      }

      if (newJoinReq.reqStatus === "Accept") {
        Alert.alert(
          "Your Request Has been Accepted by " +
            newReq.firstName +
            " " +
            newReq.lastName +
            "."
        );

        navigation.navigate("RidersViewScreen");
      }
    }
  }, [newJoinReq]);

  useFocusEffect(
    useCallback(() => {
      joinList.forEach((item) => {
        joinListdata.push(item);
      });

      return () => {
        // dispatch(setNewJoinRequest(null))

        if (isModal01Visible || isModal02Visible) {
          setisModal01Visible(false);
          setisModal02Visible(false);
        }
      };
    }, [isModal01Visible, isModal02Visible, joinList])
  );

  const HandleConfirm = async (recUId, recJReqId, reqStatus) => {
    try {
      const response = await axios.post(
        `${API_BASE_URL}/passenger/msgJoinControl`,
        {
          senderUserId: userInfo.primaryUserInfo.userid,
          senderJoinReqId: joinReqId,
          receiverUserId: recUId,
          reveiverJoinReqId: recJReqId,
          reqStatus: reqStatus,
        }
      );
      if (response) {
        console.log("Got a Response:", response.data);
        if (reqStatus == 4) {
          dispatch(
            replaceJoinListItem({
              senderJoinReqId: recUId,
              newValues: {
                reqStatus: reqStatus,
              },
            })
          );
        }

        if (reqStatus == 3) {
          navigation.navigate("RidersViewScreen");
        }
        console.log("from " + joinReqId);
      } else {
        console.log("Ride request unsuccessful:", response.data.message);
        
      }
    } catch (error) {
      console.error("Error join sending route to backend:", error);
    }
  };

  const handleItemPress = (item) => {
    setSelectedItem(item);
    setisModal01Visible(true);
  };

  const closeModal = () => {
    setisModal01Visible(false);
    setisModal02Visible(false);
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
          visible={isModal01Visible}
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
                      onPress={() =>
                        HandleConfirm(
                          selectedItem.userid,
                          selectedItem.joinReqId,
                          2
                        )
                      }
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

        <Modal
          transparent={true}
          visible={isModal02Visible}
          animationType="slide"
          onRequestClose={closeModal}
        >
          <View
            style={tw`flex-1 justify-center items-center bg-black bg-opacity-50`}
          >
            <View style={tw`w-72 p-5 bg-white rounded-lg items-center`}>
              {selectedItem && (
                <>
                  <Text style={tw`text-base mb-1 text-center`}>
                    Let's Join to Share the Taxi !
                  </Text>
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

                  <View style={tw`flex-row justify-center  w-full mt-5 mb-4`}>
                    <Button
                      mode="contained"
                      onPress={() =>
                        HandleConfirm(
                          selectedItem.userid,
                          selectedItem.joinReqId,
                          3
                        )
                      }
                    >
                      Accept
                    </Button>
                    <Button
                      mode="contained"
                      buttonColor="red"
                      onPress={() =>
                        HandleConfirm(
                          selectedItem.userid,
                          selectedItem.joinReqId,
                          4
                        )
                      }
                    >
                      Decline
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
