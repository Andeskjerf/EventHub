import axios from "axios";
import { logout } from "@/services/authentication";
import { clearAuth } from "@/services/storage";
import { userModule } from "@/stores/auth/module";

export const apiClient = axios.create({
	baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8090/api",
	withCredentials: true,
	timeout: 10000,
	headers: {
		"Content-Type": "application/json",
	},
});

let refreshAttempt = false;

apiClient.interceptors.response.use(
	(response) => response,
	async (error) => {
		if (error.response?.status === 401) {
			if (!refreshAttempt) {
				refreshAttempt = true;
				await apiClient.post("/auth/refresh");
			} else if (refreshAttempt && userModule.state.isAuthenticated) {
				await logout();
				clearAuth();
				userModule.actions.updateAuthState();
			}
			refreshAttempt = false;
		}
		return Promise.reject(error);
	},
);
