import { apiClient } from "@/api/axios";
import type { LoginCreds } from "@/models/login_creds";
import type { RegisterRequest } from "@/models/register_request";
import { userModule } from "@/stores/auth/module";
import { clearAuth, STORAGE_KEYS, setUsername } from "./storage";

const API_ENDPOINT_BASE: string = "/api/auth";
export const REGISTRATION_ENABLED: boolean =
	import.meta.env.VITE_REGISTRATION_ENABLED ?? false;

export async function login(
	username: string,
	password: string,
): Promise<string | undefined> {
	const creds: LoginCreds = {
		username,
		password,
	};

	const response = await apiClient
		.post(API_ENDPOINT_BASE + "/login", creds)
		.catch((e) => {
			// TODO: need to handle this, notify the user or whatever
			console.log(`E: failed to login: ${e}`);
			throw e.response;
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

	const response = await apiClient
		.post(API_ENDPOINT_BASE + "/register", creds)
		.catch((e) => {
			// TODO: need to handle this, notify the user or whatever
			console.log(`E: failed to register: ${e}`);
			throw e.response;
		});

	if (response?.data?.[STORAGE_KEYS.USERNAME]) {
		setUsername(username);
		userModule.actions.updateAuthState();
		return response.data[STORAGE_KEYS.USERNAME];
	}
}

export async function logout() {
	await apiClient
		.get(API_ENDPOINT_BASE + "/logout")
		.catch((e) => {
			// TODO: need to handle this, notify the user or whatever
			console.log(`E: failed to logout: ${e}`);
			throw e.response.data;
		});

	clearAuth();
	userModule.actions.updateAuthState();
}
