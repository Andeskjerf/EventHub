import axios from "axios";
import { clearAuth } from "@/services/storage";
import { userModule } from "@/stores/auth/module";

export const apiClient = axios.create({
	baseURL: import.meta.env.API_URL || "http://localhost:8090",
	withCredentials: true,
	timeout: 10000,
	headers: {
		"Content-Type": "application/json",
	},
});

apiClient.interceptors.response.use(
	(response) => response,
	(error) => {
		if (error.response?.status === 401) {
			clearAuth();
			userModule.actions.updateAuthState();
		}
		return Promise.reject(error); // Re-throw the error
	},
);
