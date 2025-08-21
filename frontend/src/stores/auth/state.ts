export interface IUserState {
	id: number | null;
	token: string | null;
}

export const getInitialUserState = (): IUserState => {
	return {
		id: 1,
		token: "some-token-the-backend-uses-to-auth",
	};
};

const moduleState = getInitialUserState();
export default moduleState;
