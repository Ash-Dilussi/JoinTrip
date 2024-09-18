import React from 'react'
import { FlatList, Image, SafeAreaView, StyleSheet, Text, TouchableOpacity, View } from 'react-native'
import { GooglePlacesAutocomplete } from 'react-native-google-places-autocomplete';
import { useDispatch, useSelector } from 'react-redux';
import tw from 'twrnc';
import { selectDestination, setDestination } from '../slices/navSlice';
import { GOOGLE_MAPS_APIKEY } from "@env";
import { useNavigation } from '@react-navigation/native';


const MapNavigationCard = () => {

    const dispatch = useDispatch();
    const destest = useSelector(selectDestination);
    const navigation = useNavigation();
    return (
        <View>
            <Text style={tw`text-center py-5 text-xl`}>Good Day</Text>
            <View>
                <GooglePlacesAutocomplete

                    styles={{ container: { flex: 0 }, textInput: { fontSize: 18, backgroundColor: "#F5F5F5" }, textInputContainer: { paddingHorizontal: 20, paddingBottom: 1 } }}

                    fetchDetails={true}
                    returnKeyType={"search"}
                    query={{
                        key: GOOGLE_MAPS_APIKEY,
                        language: 'en',
                    }}

                    onPress={(data, details = null) => {

                        dispatch(setDestination({
                            locatoin: details.geometry.location,
                            description: data.description,
                        }));

                        navigation.navigate("SelectOptionCard")
                      
                    }}
                    placeholder='Where to?'
                    debounce={400} />
            </View>
        </View>
    )
}

export default MapNavigationCard