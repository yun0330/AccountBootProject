import "./Login.css"
import axios from "axios";
import React, {useState,useEffect} from "react";
import { FaUser,FaLock } from "react-icons/fa";
import { Link,useNavigate } from "react-router-dom"
import { API_BASE_URL } from "../api_config";
import { useAuth } from "../Utils/AuthContext";

const Login = () => {
    const [userId, setUserId] = useState("");
    const [userPw, setUserPw] = useState("");
    const navigate = useNavigate();
    const { login } = useAuth();

    const handleLogin = async (event) => {
        event.preventDefault();
        const userDTO = {
            userId,
            userPw
        };
        try {
            const request = await axios.post(`${API_BASE_URL}/member/login`, userDTO , {
                headers: {"Content-Type": "application/json"}
            });
            const { userId, accessToken, refreshToken } = request.data;
            login({
                userId,
                accessToken,
                refreshToken
            });
            if (request.status === 200) {
                navigate("/");
            }
            } catch (error) {
                const serverMessage = error.response?.data.error || "로그인 처리 중 오류가 발생했습니다.";
                alert(serverMessage);
                }
            }
            
    const userIdChange = (e) => {
        setUserId(e.target.value);
    }

    const userPwChange = (e) => {
        setUserPw(e.target.value);
    }

    return(
    <div className="Login">
    <div className="login_logo">
    <Link to="/"><img img src="assets/WalletStory_logo.png" alt="logo"/></Link>
    </div>
    <div className="login_box">
    <form noValidate onSubmit={handleLogin}>
    <div className="login_input">
    <label>
    <p className="login_icon"><FaUser/></p>
    <input type="text" placeholder="아이디" maxLength={20} id="userId" name="userId" className="login_item" value={userId} onChange={userIdChange}/>
    </label>
    <label>
    <p className="login_icon"><FaLock/></p>
    <input type="password" placeholder="비밀번호" maxLength={20} id="userPw" name="userPw" className="login_item" value={userPw} onChange={userPwChange}/>
    </label>
    </div>
    <div className="login_btn_area">
    <button className="login_btn">로그인</button>
    </div>
    </form>
    <div className="login_for">
    <Link to="/" className="find_text"><p>아이디 찾기</p></Link>
    <p className="stick">|</p>
    <Link to="/" className="find_text"><p>비밀번호 찾기</p></Link>
    <p className="stick">|</p>
    <Link to="/" className="find_text"><p className="find_text_purple">회원가입</p></Link>
    </div>
    </div>
    </div>
    )
}

export default Login;