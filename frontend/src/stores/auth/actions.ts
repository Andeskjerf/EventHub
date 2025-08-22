import { getUsername, isAuthenticated } from "@/services/storage";
import { userModule } from "./module";

const updateAuthState = () => {
	userModule.state.username = getUsername();
	userModule.state.isAuthenticated = isAuthenticated();
};

export default {
	updateAuthState,
};
