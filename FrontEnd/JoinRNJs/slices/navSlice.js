import { createSlice } from "@reduxjs/toolkit";

const initialState= {
    origin:null,
    destination: null,
    travelTimeInformation: null,
    farRoute: null,
    rideType: null,
    scheduleTime: null,
    userType: 0,
    userInfo: null
}

export const navSlice = createSlice({
    name: 'nav',
    initialState,
    reducers:{
        setOrigin: (state, action) =>{
            state.origin = action.payload;
        },
        setDestination:(state,action)=>{
            state.destination = action.payload;
        },
        setTravelTimeInformation:(state, action)=>{
            state.travelTimeInformation = action.payload;
        },
        setFarRoute:(state,action)=>{
            state.farRoute = action.payload;
        }, 
        setRideType:(state,action)=>{
            state.rideType = action.payload;
        }, 
        setScheduleTime:(state,action)=>{
            state.scheduleTime = action.payload;
        },  
        setUserType:(state,action)=>{
            state.userType = action.payload;
        },
        setUserInfo:(state,action)=>{
            state.userInfo = action.payload;
        },

    },
});

export const {setOrigin, setDestination, setTravelTimeInformation, setFarRoute, setRideType, setScheduleTime, setUserType, setUserInfo}= navSlice.actions;

//selectors: import data from the global states
export const selectOrigin = (state) => state.nav.origin;
export const selectDestination = (state) => state.nav.destination;
export const selectTravelTimeInformation = (state) => state.nav.travelTimeInformation;
export const selectFarRoute = (state) => state.nav.farRoute;
export const selectRideType = (state) => state.nav.rideType;
export const selectScheduleTime = (state) => state.nav.scheduleTime;
export const selectUserType = (state) => state.nav.userType;
export const selectUserInfo = (state) => state.nav.userInfo;


export default navSlice.reducer;