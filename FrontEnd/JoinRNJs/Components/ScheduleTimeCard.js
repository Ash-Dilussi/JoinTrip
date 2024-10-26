import React, { useState } from 'react';
import { 
  Text,
  TouchableOpacity,
  View,
  Platform
} from "react-native";

import { useDispatch, useSelector } from "react-redux";
import tw from "twrnc";
import DateTimePicker from '@react-native-community/datetimepicker'; 
import { useNavigation } from "@react-navigation/native";
import { Icon } from "react-native-elements";
import { setScheduleTime } from '../slices/navSlice';

const ScheduleTimeCard = () => {
  const dispatch = useDispatch();
  const navigation = useNavigation();

  const [date, setDate] = useState(new Date());
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [showTimePicker, setShowTimePicker] = useState(false);

  const onDateChange = (event, selectedDate) => {
    const currentDate = selectedDate || date;
    setShowDatePicker(Platform.OS === 'ios');
    setDate(currentDate);
  };

  const onTimeChange = (event, selectedTime) => {
    const currentTime = selectedTime || date;
    setShowTimePicker(Platform.OS === 'ios');
    setDate(currentTime);
  };


  const handlesubmit = () =>{

    const scheduleTime = new Date(date).getTime(); // Get timestamp

    dispatch(setScheduleTime({
        datetime:scheduleTime
    }))
    console.log("schedulte  time: "+ scheduleTime)
    //console.log("schedulte  timet2: "+ date.toLocaleString())
    navigation.navigate("SelectOptionCard")

  }

  return (
    
<View style={tw`flex-1 justify-center items-center bg-gray-100 p-6`}>
      <Text style={tw`text-2xl font-bold mb-10`}>Set Schedule Trip Time</Text>
       
      {/* Display selected Date */}
      <Text style={tw`text-lg mb-4`}>
        Selected Date: {date.toLocaleDateString()}
      </Text>

      {/* Display selected Time */}
      <Text style={tw`text-lg mb-4`}>
        Selected Time: {date.toLocaleTimeString()}
      </Text>

      {/* Button to open Date Picker */}
      <TouchableOpacity
        style={tw`bg-blue-500 px-6 py-2 rounded-full mb-4`}
        onPress={() => setShowDatePicker(true)}
      >
        <Text style={tw`text-white text-lg`}>Pick Date</Text>
      </TouchableOpacity>

      {/* Button to open Time Picker */}
      <TouchableOpacity
        style={tw`bg-green-500 px-6 py-2 rounded-full`}
        onPress={() => setShowTimePicker(true)}
      >
        <Text style={tw`text-white text-lg`}>Pick Time</Text>
      </TouchableOpacity>

      {/* Date Picker */}
      {showDatePicker && (
        <DateTimePicker
          value={date}
          mode="date"
          display="default"
          onChange={onDateChange}
        />
      )}

      {/* Time Picker */}
      {showTimePicker && (
        <DateTimePicker
          value={date}
          mode="time"
          display="default"
          onChange={onTimeChange}
        />
      )}

      <View>
        <TouchableOpacity
          style={tw`mt-0 mx-0`}
          onPress={handlesubmit}
        >
          <Icon
            style={tw`p-3 bg-black  rounded-full w-12 ml-80 mt-1`}
            name="arrowright"
            color="white"
            type="antdesign"
          />
        </TouchableOpacity>
      </View>

    </View>


  );
};

export default ScheduleTimeCard;
