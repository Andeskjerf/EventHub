import { getUsername } from "@/services/storage";
import { userModule } from "./module";

const updateAuthState = () => {
	userModule.state.username = getUsername();
};

export default {
	updateAuthState,
};
