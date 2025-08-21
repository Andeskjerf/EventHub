import { userModule } from "./module";

/**
 * @returns currently logged in user's ID, null if not logged in
 */
const getUserId = (): number | null => {
	return userModule.state.id;
};

export default {
	getUserId,
};
