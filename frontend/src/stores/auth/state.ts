import { getUsername, isAuthenticated } from "@/services/storage";

export interface IUserState {
	username: string | null;
	isAuthenticated: boolean;
}

export const getInitialUserState = (): IUserState => {
	return {
		username: getUsername(),
		isAuthenticated: isAuthenticated(),
	};
};

const moduleState = getInitialUserState();
export default moduleState;
