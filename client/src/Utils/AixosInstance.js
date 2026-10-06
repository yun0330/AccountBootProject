import axios from "axios";
import { API_BASE_URL } from "../api_config";
import { refreshAccessToken } from "./refresh";

const AixosInstance = axios.create({
    baseURL: API_BASE_URL,
    headers: {"Content-Type": "application/json"}
});

AixosInstance.interceptors.request.use((config) => {
    const accessToken = localStorage.getItem("ACCESS_TOKEN");

    if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
    }

    return config;
});

AixosInstance.interceptors.response.use(
    (response) => response,async(error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                const newAccessToken = await refreshAccessToken();

                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
                return AixosInstance(originalRequest);
            } catch (refreshError) {
                localStorage.removeItem("user");
                localStorage.removeItem("ACCESS_TOKEN");
                localStorage.removeItem("REFRESH_TOKEN");

                window.location.href = "/login";

                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

export default AixosInstance;