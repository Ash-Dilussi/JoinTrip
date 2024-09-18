import { configureStore } from '@reduxjs/toolkit'
import navReducer from './slices/navSlice'
//import filtersReducer from '../features/filters/filtersSlice'

export const store = configureStore({
  reducer: {
    nav: navReducer,
   // filters: filtersReducer,
  },
})