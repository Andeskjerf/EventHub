import { reactive } from "vue";
import actions from "./actions";
import getters from "./getters";
import state from "./state";

export const userModule = {
	state: reactive(state),
	actions,
	getters,
};
