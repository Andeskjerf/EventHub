import axios, { type AxiosResponse } from "axios";
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

let refreshPromise: Promise<AxiosResponse<any, any>> | null = null;

apiClient.interceptors.response.use(
	(response) => {
		if (refreshPromise) refreshPromise = null;
		return response;
	},
	async (error) => {
		const originalRequest = error.config;

		if (error.response?.status === 401 && !originalRequest._retry) {
			originalRequest._retry = true;

			if (!refreshPromise) {
				refreshPromise = apiClient
					.post("/auth/refresh")
					.catch(async (refreshError) => {
						refreshPromise = null;
						if (userModule.state.isAuthenticated) {
							await logout();
							clearAuth();
							userModule.actions.updateAuthState();
						}
						throw refreshError;
					});
			}

			try {
				await refreshPromise;
				return apiClient(originalRequest);
			} catch (refreshError) {
				return Promise.reject(refreshError);
			}
		}

		return Promise.reject(error);
	},
);
