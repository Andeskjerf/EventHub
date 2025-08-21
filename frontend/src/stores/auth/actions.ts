import { userModule } from "./module";

/**
 * Logs the user in
 * @param TODO - need to add some credentials here at some point
 */
const login = () => {
	userModule.state.id = 0;
	userModule.state.token = "we-logged-in-using-a-fake-token-yippiiee";
};

// TODO: no backend yet
const logout = () => {
	userModule.state.id = null;
	userModule.state.token = null;
};

export default {
	login,
	logout,
};
