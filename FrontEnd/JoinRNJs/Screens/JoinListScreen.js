import React, { useState,useCallback } from 'react';
import { View, Text, FlatList, TouchableOpacity, Modal } from 'react-native';
import { Button, Provider as PaperProvider } from 'react-native-paper';
import { useSelector } from 'react-redux'; 
import { selectJoinList } from '../slices/navSlice';
import tw from 'twrnc';
import { useFocusEffect } from '@react-navigation/native';

const JoinListScreen = () => {
  const [selectedItem, setSelectedItem] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);

  const joinList = useSelector(selectJoinList);

  const data = [
    { id: '1', name: 'John Doe', info: 'Additional information about John Doe' },
    { id: '2', name: 'Jane Smith', info: 'Additional information about Jane Smith' },
    { id: '3', name: 'Sam Wilson', info: 'Additional information about Sam Wilson' },
  ];
  const joinListdata = [];


  
  useFocusEffect(
    useCallback(() => {
     // console.log("home focused!"); 
     joinList.forEach((item)=>{
        joinList.push(item)
     })
     
      return () => {
       // console.log(" home navigating away.");
      };
    }, [])
  );

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
                  <Text style={tw`text-xl font-bold mb-2`}>{selectedItem.firstName} {selectedItem.lastName}</Text>
                  <Text style={tw`text-base mb-5 text-center`}>{selectedItem.town}</Text>
                  <Text style={tw`text-base mb-5 text-center`}>{selectedItem.gender}</Text>
                  <Text style={tw`text-base mb-5 text-center`}>{selectedItem.phone}</Text>
                  
                  <View style={tw`flex-row justify-between w-full mb-4`}>
                    <Button mode="contained" onPress={() => console.log(joinList)}>
                      Join Me!
                    </Button>
                    {/* <Button mode="outlined" onPress={() => console.log("Action 2")}>
                      Action 2
                    </Button> */}
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
