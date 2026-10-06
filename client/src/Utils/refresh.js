import axios from "axios";
import { API_BASE_URL } from "../api_config";

export const refreshAccessToken = async () => {
    const refreshToken = localStorage.getItem("REFRESH_TOKEN");

    if (!refreshToken) {
        throw new Error("Refresh Token이 없습니다.");
    }

    const request = await axios.post(`${API_BASE_URL}/member/refresh`, {
        refreshToken
    }, {
        headers: {"Content-Type": "application/json"}
    });

    const {
        userId,
        accessToken,
        refreshToken: newRefreshToken
    } = request.data;

    localStorage.setItem("user", JSON.stringify({ userId }));
    localStorage.setItem("ACCESS_TOKEN", accessToken);
    localStorage.setItem("REFRESH_TOKEN", newRefreshToken);

    return accessToken;
};