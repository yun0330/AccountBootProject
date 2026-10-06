import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [accessToken, setAccessToken] = useState(null);
    const [refreshToken, setRefreshToken] = useState(null);

    const login = ({ userId, accessToken, refreshToken}) => {
        const userData = { userId };

        setUser(userData);
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);

        localStorage.setItem("user", JSON.stringify(userData));
        localStorage.setItem("ACCESS_TOKEN", accessToken);
        localStorage.setItem("REFRESH_TOKEN", refreshToken);
    };

    const logout = () => {
        setUser(null);
        setAccessToken(null);
        setRefreshToken(null);

        localStorage.removeItem("user");
        localStorage.removeItem("ACCESS_TOKEN");
        localStorage.removeItem("REFRESH_TOKEN");
    };
    const provision = {
        user,
        accessToken,
        refreshToken,
        login,
        logout,
        isAuthenticated: !!user && !!accessToken
    };

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        const storedAccessToken = localStorage.getItem("ACCESS_TOKEN");
        const storedRefreshToken = localStorage.getItem("REFRESH_TOKEN");

        if (storedUser && storedAccessToken && storedRefreshToken) {
            setUser(JSON.parse(storedUser));
            setAccessToken(storedAccessToken);
            setRefreshToken(storedRefreshToken);
        }
    }, []);

    return (
        <AuthContext.Provider value={provision}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
}