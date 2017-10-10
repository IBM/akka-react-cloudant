export default function (state={}, action) {
  switch(action.type) {
    case "HEADER_BANNER_STYLE_SELECTED":
      return action.payload;
    default:
      return state;
  }
}
