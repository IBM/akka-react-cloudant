import {combineReducers} from 'redux';
import headerStyleReducers from './style-switcher-reducers/headerStyleReducers.js';
import headerActiveStyleReducer from './style-switcher-reducers/headeractiveReducer.js';
import sidebarStyleReducers from './style-switcher-reducers/sidebarStyleReducers.js';
import sidebarActiveStyleReducer from './style-switcher-reducers/sidebarActiveReducer.js';
import headerBannerStyleReducers from './style-switcher-reducers/headerBannerStyleReducers.js';
import headerBannerActiveStyleReducer from './style-switcher-reducers/headerBannerActiveReducer.js';

const allReducers = combineReducers({  
  headerStyle: headerStyleReducers,
  headerActiveStyle: headerActiveStyleReducer,
  headerBannerStyle: headerBannerStyleReducers,
  headerBAnnerActiveStyle: headerBannerActiveStyleReducer,
  sidebarStyle: sidebarStyleReducers,
  sidebarActiveStyle: sidebarActiveStyleReducer,
});

export default allReducers;
