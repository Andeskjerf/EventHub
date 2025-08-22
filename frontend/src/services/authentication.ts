import axios from "axios";
import { useRouter } from "vue-router";
import { BACKEND_URL } from "@/consts/backend";
import type { LoginCreds } from "@/models/login_creds";
import type { RegisterRequest } from "@/models/register_request";
import { userModule } from "@/stores/auth/module";
import { clearAuth, STORAGE_KEYS, setUsername } from "./storage";

const API_ENDPOINT_BASE: string = "/api/auth";

const router = useRouter();

axios.interceptors.response.use(
	(response) => response,
	(error) => {
		if (error.response?.status === 401) {
			clearAuth();
			userModule.actions.updateAuthState();
			router.push("/auth");
		}
		return Promise.reject(error); // Re-throw the error
	},
);

export async function login(
	username: string,
	password: string,
): Promise<string | undefined> {
	const creds: LoginCreds = {
		username,
		password,
	};

	const response = await axios
		.post(BACKEND_URL + API_ENDPOINT_BASE + "/login", creds)
		.catch((e) => {
			// TODO: need to handle this, notify the user or whatever
			console.log(e);
			return null;
		});

	if (response?.data?.[STORAGE_KEYS.USERNAME]) {
		setUsername(username);
		userModule.actions.updateAuthState();
		return response.data[STORAGE_KEYS.USERNAME];
	}
}

export async function register(
	email: string,
	username: string,
	password: string,
): Promise<string | undefined> {
	const creds: RegisterRequest = {
		email,
		username,
		password,
	};

	const response = await axios
		.post(BACKEND_URL + API_ENDPOINT_BASE + "/register", creds)
		.catch((e) => {
			// TODO: need to handle this, notify the user or whatever
			console.log(e);
			return null;
		});

	if (response?.data?.[STORAGE_KEYS.USERNAME]) {
		setUsername(username);
		userModule.actions.updateAuthState();
		return response.data[STORAGE_KEYS.USERNAME];
	}
}
