import { userModule } from "./module";

/**
 * @returns currently logged in user's ID, null if not logged in
 */
const getUsername = (): string | null => {
	return userModule.state.username;
};

export default {
	getUsername,
};
