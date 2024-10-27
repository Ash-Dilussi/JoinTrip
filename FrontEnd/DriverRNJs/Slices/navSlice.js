import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  currentLocation: null,
};

export const navSlice = createSlice({
  name: "nav",
  initialState,
  reducers: {
    setCurrentLocation: (state, action) => {
      state.currentLocation = action.payload;
    },
  },
});

export const { setCurrentLocation } = navSlice.actions;

export const selectCurrentLocation = (state) => state.nav.currentLocation;

export default navSlice.reducer;
