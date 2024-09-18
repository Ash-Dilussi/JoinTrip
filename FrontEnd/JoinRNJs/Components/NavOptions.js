import React from 'react'
import { FlatList, Image, SafeAreaView, StyleSheet, Text, TouchableOpacity, View, Alert } from 'react-native'

import UberXImage from '../assets/favicon.png';
import tw from 'twrnc';
import { Icon } from 'react-native-elements';
import { useNavigation } from '@react-navigation/native';
import { useSelector } from 'react-redux';
import { selectOrigin } from '../slices/navSlice';


const data =[
    {
        id: "123",
        title: "Get a Ride",
        image: UberXImage,
        screen: "MapScreen"

    },
    {
        id: "456",
        title: "Join Another",
        image: "http://links.papareact.com/3pn",
        screen: "JoinOther"

    }
]


const NavOptions = () =>{

    const navigation= useNavigation();
    const origin = useSelector(selectOrigin);

    return (
       

        <FlatList
        data= {data}
        keyExtractor={(item)=> item.id}
        horizontal
        renderItem={({item})=>(
            <TouchableOpacity
            onPress={()=>{ 
            
            if(!origin){Alert.alert("No Location Selected.")}
            else{navigation.navigate(item.screen)}
            }}
            style={tw`m-2 w-40 pl-6 p-2`}>
                <View>
                    <Image
                    style={{width:120, height:120, resizeMode: "contain"}}
                    source={{url: item.image}}
                   
                    />
                </View>
                <Text style= {tw `mt-2 text-lg font-semibold`}>{item.title}</Text>
                <Icon 
                style={tw`p-2 bg-black rounded-full w-10 mt-4`}
                name="arrowright"
                color="white"
                type="antdesign"/>
            </TouchableOpacity>
        )}
        />
    );
}

export default NavOptions