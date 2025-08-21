import { getToken, getUsername } from "@/services/storage";
import { userModule } from "./module";

const updateAuthState = () => {
	userModule.state.username = getUsername();
	userModule.state.token = getToken();
};

export default {
	updateAuthState,
};
