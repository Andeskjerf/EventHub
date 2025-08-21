import axios from "axios";
import { BACKEND_URL } from "@/consts/backend";
import type { LoginCreds } from "@/models/login_creds";
import type { RegisterRequest } from "@/models/register_request";

const API_ENDPOINT_BASE: string = "/api/auth";

export async function login(username: string, password: string) {
	const creds: LoginCreds = {
		username,
		password,
	};

	const response = await axios.post(
		BACKEND_URL + API_ENDPOINT_BASE + "/login",
		creds,
	);

	console.log(response);
}

export async function register(
	email: string,
	username: string,
	password: string,
) {
	const creds: RegisterRequest = {
		email,
		username,
		password,
	};

	const response = await axios.post(
		BACKEND_URL + API_ENDPOINT_BASE + "/register",
		creds,
	);

	console.log(response);
}
