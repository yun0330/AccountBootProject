import "./Membership.css";
import React, {useState,useEffect} from "react";
import axios from "axios";
import {API_BASE_URL} from "../api_config";
const Membership = () => {

    const [userId, setUserId] = useState("");
    const [userIdError, setUserIdError] = useState("");
    const [userIdRequest, setUserIdRequest] = useState(false);
    const [userPw, setUserPw] = useState("");
    const [userPwError, setUserPwError] = useState("");
    const [confirmPw, setConfirmPw] = useState("");
    const [confirmPwError, setConfirmPwError] = useState("");
    const [userName, setUserName] = useState("");
    const [userNameError, setUserNameError] = useState("");
    const [userEmail, setUserEmail] = useState("");
    const [userEmailError, setUserEmailError] = useState("");
    const [userPhone, setUserPhone] = useState("");
    const [userPhoneError, setUserPhoneError] = useState("");
    const [nickName, setNickName] = useState("");
    const [nickNameError, setNickNameError] = useState("");
    const [formValid, setFormValid] = useState(false);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const userDTO = {
            userId,
            userPw, 
            userEmail,
            userName, 
            userPhone, 
            nickName
        };
        try {
            const request = await axios.post(`${API_BASE_URL}/member/createuser`,userDTO, {
                headers: {"Content-Type": "application/json"}
            });
            if (request.status === 200) {
                alert("회원가입이 완료되었습니다.")
            }
        } catch (error) {
            console.error("서버 응답 오류:", error.response?.data || error.message);
        }
    };

    useEffect(() => {
        const userIdValid = userIdError === "사용 가능한 아이디 입니다." && userIdRequest;
        const userPwValid = userPw && !userPwError;
        const confirmPwValid = confirmPw && !confirmPwError && userPw === confirmPw;
        const userNameValid = userName && !userNameError;
        const userEmailValid = userEmail && !userEmailError;
        const userPhoneValid = userPhone && !userPhoneError;
        const nickNameValid = nickName && !nickNameError;
        setFormValid(
            userIdValid &&
            userPwValid &&
            confirmPwValid &&
            userNameValid &&
            userEmailValid &&
            userPhoneValid &&
            nickNameValid
        );
}, [userId,userIdRequest,userIdError,
userPw,userPwError,confirmPw,
userName,userNameError,
userEmail,userEmailError,
userPhone,userPhoneError,
nickName, nickNameError]);

    const rangeUserId = (userId) => {
        const range = /^[a-z0-9]{4,20}$/;
        return range.test(userId);
    };
    const userIdChange = (e) => {
        const handleId = e.target.value;
        setUserId(handleId);

        if(!rangeUserId(handleId)) {
            setUserIdError("아이디는 최소 4~20글자여야 합니다.")
        }
        else {
            setUserIdError("");
            setUserIdRequest(false);
        }
    };

    const userIdOverlap = async() => {
        if(!userId) {
            setUserIdError("아이디를 입력하세요.");
            return;
        }
        try {
            const request = await axios.get(`${API_BASE_URL}/member/checkid`,{
                params: {userId}
            });
            if(request.data) {
                setUserIdError("이미 사용중인 아이디 입니다.");
                setUserIdRequest(false);
            }
            else {
                setUserIdError("사용 가능한 아이디 입니다.");
                setUserIdRequest(true);
            }
        }
        catch(error) {
            console.error("중복 확인 오류:", error.request?.data || error.message);
        }
    }

    const rangeUserPw = (userPw) => {
        const range = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@.#$%^&*]).{8,20}$/;
        return range.test(userPw);
    };
    const userPwChange = (e) => {
        const handlePw = e.target.value;
        setUserPw(handlePw);

        if(!rangeUserPw(handlePw)) {
            setUserPwError("형식에 맞지 않습니다. 8~20자리 영문, 숫자, 특수문자 조합이어야 합니다.");
        }
        else {
            setUserPwError("");
        }
    };

    const confirmPwChange = (e) => {
        const handleConfirmPw = e.target.value;
        setConfirmPw(handleConfirmPw);

        if(handleConfirmPw !== userPw) {
            setConfirmPwError("비밀번호가 일치하지 않습니다.")
        }
        else {
            setConfirmPwError("");
        }
    }

    const rangeUserName = (userName) => {
        const range = /^[가-힣]{2,20}$/;
        return range.test(userName);
    };

    const userNameChange = (e) => {
        const handleUserName = e.target.value;
        setUserName(handleUserName);

        if(!rangeUserName(handleUserName)) {
            setUserNameError("이름은 한글이어야 합니다.")
        }
        else {
            setUserNameError("");
        }
    };

    const rangeUserEmail = (userEmail) => {
        const range = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return range.test(userEmail);
    };

    const userEamilChange = (e) => {
        const handleUserEmail = e.target.value;
        setUserEmail(handleUserEmail);

        if(!rangeUserEmail(handleUserEmail)) {
            setUserEmailError("이메일 형식이 맞지 않습니다.")
        }
        else {
            setUserEmailError("");
        }
    };

    const rangeUserPhone =  (userPhone) => {
        const range = /^[0-9]{11}$/;
        return range.test(userPhone);
    };

    const userPhoneChange = (e) => {
        const handleUserPhone = e.target.value;
        setUserPhone(handleUserPhone);

        if(!rangeUserPhone(handleUserPhone)) {
            setUserPhoneError("휴대폰 번호는 숫자만 입력해주세요")
        }
        else {
            setUserPhoneError("");
        }
    };

    const rangeNickName = (nickName) => {
        const range = /^[a-zA-Z0-9가-힣]+$/;
        return range.test(nickName);
    }

    const nickNameChange = (e) => {
        const handleNickName = e.target.value;
        setNickName(handleNickName);

        if(!rangeNickName(handleNickName)) {
            setNickNameError("특수문자를 제거해주세요")
        }
        else {
            setNickNameError("");
        }
    }

    return(
        <div>
        <div className="Membership_box">
        <div className="member_title">
            <h1>회원가입</h1>
        </div>
        <form onSubmit={handleSubmit}>
        <div className="member_info">
        <div className="member_input">
        <label>
            <p className="member_wrap">
            <input type="text" title="아이디" placeholder="아이디" maxLength={20} id="userId" name="userId" className="member_item_id" value={userId} onChange={userIdChange} />
            <button type="button" className="overlap" onClick={userIdOverlap}>중복확인</button>
            </p>
        </label>
        {userIdError && (
            <div className={`member_msg_error ${userIdRequest ? "success":"error"}`}>
                {userIdError}
            </div>
        )}
        <div className="member_msg">4~20자리 영문 소문자, 숫자 조합만 가능합니다.</div>
        </div>
        <div className="member_input">
        <label>
            <p className="member_wrap">
            <input type="password" title="비밀번호" placeholder="비밀번호" maxLength={20} id="userPw" name="userPw" className="member_item" value={userPw} onChange={userPwChange} />
            </p>
        </label>
        {userPwError && <div className="member_msg_error">{userPwError}</div>}
        <div className="member_msg">8~20자리 영문, 숫자, 특수문자(!@.#$%^&*)조합</div>
        </div>
        <div className="member_input">
        <label>
            <p className="member_wrap">
            <input type="password" title="비밀번호 확인" placeholder="비밀번호 확인" maxLength={20} id="passwordCheck" className="member_item" value={confirmPw} onChange={confirmPwChange} />
            </p>
        </label>
        {confirmPwError && <div className="member_msg_error">{confirmPwError}</div>}
        </div>
        <div className="member_input">
        <label>
            <p className="member_wrap">
            <input type="text" title="이름" placeholder="이름" maxLength={20} id="userName" className="member_item" name="userName" value={userName} onChange={userNameChange}/>
            </p>
        </label>
        {userNameError && <div className="member_msg_error">{userNameError}</div>}
        </div>
        <div className="member_input">
        <label>
            <p className="member_wrap">
            <input type="email" title="이메일" placeholder="이메일 (예:walletstory@walletstory.com)" maxLength={200} id="user_Email" name="userEmail" className="member_item" value={userEmail} onChange={userEamilChange}/>
            </p>
        </label>
        {userEmailError && <div className="member_msg_error">{userEmailError}</div>}
        </div>
        <div className="member_input">
        <label>
            <p className="member_wrap">
            <input type="tel" title="전화번호" placeholder="휴대폰 번호(-제외)" maxLength={11} id="userPhone" className="member_item" name="userPhone" value={userPhone} onChange={userPhoneChange}/>
            </p>
        </label>
        {userPhoneError && <div className="member_msg_error">{userPhoneError}</div>}
        </div>
        <div className="member_input">
        <label>
            <p className="member_wrap">
            <input type="text" title="닉네임" placeholder="닉네임" maxLength={20} id="nickName" className="member_item" name="nickName" value={nickName} onChange={nickNameChange}/>
            </p>
        </label>
        {nickNameError && <div className="member_msg_error">{nickNameError}</div>}
        <div className="member_msg">2~20자 안으로 가능합니다.</div>
        </div>
        <div className="member_btn_area">
            <button className="member_btn" disabled={!formValid}>회원가입</button>
        </div>
        </div>
        </form>
        </div>
        </div>
    )
}

export default Membership;