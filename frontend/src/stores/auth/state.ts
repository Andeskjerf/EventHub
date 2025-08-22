import { getUsername } from "@/services/storage";

export interface IUserState {
	username: string | null;
}

export const getInitialUserState = (): IUserState => {
	return {
		username: getUsername(),
	};
};

const moduleState = getInitialUserState();
export default moduleState;
