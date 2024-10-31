import React, { useEffect, useState } from 'react';
import { View, Text, FlatList,   TouchableOpacity, Modal } from 'react-native';
import WebSocketService from '../Components/WebSocketService';
import { Button, Provider as PaperProvider } from 'react-native-paper';
import tw from 'twrnc';
import { useDispatch, useSelector } from "react-redux";
import { selectUserInfo, selectTripreqList } from "../Slices/navSlice";

const TripRequestList = () => { 
 
   
  const [selectedItem, setSelectedItem] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);

const userInfo = useSelector(selectUserInfo);
const tripReqList = useSelector(selectTripreqList);

const passReqListdata = [];

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
    WebSocketService.connect(userInfo);
    

    return () => {
      WebSocketService.disconnect();
    };
  }, []);


  useEffect(() => {
 
   console.log(tripReqList)
if(!tripReqList || tripReqList.length === 0){
  tripReqList.forEach((item)=>{
    passReqListdata.push(item)
   })
}
 
 
    return () => {
      
    };
  }, [tripReqList]);


  
  if (!passReqListdata || passReqListdata.length === 0) {
    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text style={tw`text-2xl font-bold mb-5 text-center`}>Taxi Ride List</Text>
        <Text>No trip requests available.</Text>
      </View>
    );
  }

  return (
    <PaperProvider>
      <View style={tw`flex-1 pt-12 px-5`}>
        <Text style={tw`text-2xl font-bold mb-5 text-center`}>Join List</Text>
        
        <FlatList
          data={passReqListdata}
          keyExtractor={(item) => item.userid}
          renderItem={({ item }) => (
            <TouchableOpacity style={tw`p-4 bg-gray-100 border-b border-gray-300 rounded-lg mb-2`} onPress={() => handleItemPress(item)}>
              <Text style={tw`text-lg`}>{item.firstName} {item.lastName}</Text>
            </TouchableOpacity>
          )}
        />

        <Modal
          transparent={true}
          visible={isModalVisible}
          animationType="slide"
          onRequestClose={closeModal}
        >
          <View style={tw`flex-1 justify-center items-center bg-black bg-opacity-50`}>
            <View style={tw`w-72 p-5 bg-white rounded-lg items-center`}>
              {selectedItem && (
                <>
                  <Text style={tw`text-xl font-bold mb-5`}>{selectedItem.firstName} {selectedItem.lastName}</Text>
                  <Text style={tw`text-base mb-1 text-center`}>{selectedItem.taxiRequest.JoinReqid}</Text>
                  <Text style={tw`text-base mb-1 text-center`}>{selectedItem.taxiRequest.destinationName}</Text>
                  <Text style={tw`text-base mb-1 text-center`}>{selectedItem.phone}</Text>
                  <Text style={tw`text-base mb-1 text-center`}>Here waiting for taxi</Text>
                  
                  <View style={tw`flex-row justify-center  w-full mt-5 mb-4`}>
                    <Button mode="contained" onPress={() => console.log(joinList)}>
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

export default TripRequestList;
