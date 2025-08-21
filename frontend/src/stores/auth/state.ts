import { getToken, getUsername } from "@/services/storage";

export interface IUserState {
	username: string | null;
	token: string | null;
}

export const getInitialUserState = (): IUserState => {
	return {
		username: getUsername(),
		token: getToken(),
	};
};

const moduleState = getInitialUserState();
export default moduleState;
