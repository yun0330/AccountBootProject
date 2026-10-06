import "./Consent.css"
import "../UI/Modal.css"
import Tos from "../Components/Tos/Tos";
import TosCollection from "../Components/Tos/TosCollection";
import TosChoice from "../Components/Tos/TosChoice";
import React, { useState,useEffect } from "react";
import Modal from "react-modal"
import { GoArrowRight } from "react-icons/go";
import { useNavigate } from "react-router-dom";

const Consent = () => {
    const [checkBox, setCheckBox] = useState([]); 
    const [activate, setActivate] = useState(false);
    const [modalOpen, setModalOpen] = useState(false);
    const [modalType, setModalType] = useState(null);
    const navigate = useNavigate();

    const checkAll = (e) => {
        if(checkBox.length === 3) {
            setActivate("button")
        }
        else {
            setActivate("activate");
        }
        e.target.checked ? setCheckBox(["check_1","check_2","check_3"]):setCheckBox([]);
    };
    const handleCheck = (e) => {
        e.target.checked ?
        setCheckBox([...checkBox,e.target.name])
        :setCheckBox(checkBox.filter((el) => el !== e.target.name));
    };
    useEffect(() =>{
        if(checkBox.includes("check_1") && checkBox.includes("check_2"))
            setActivate(true);
        else {
            setActivate(false);
        }},[checkBox]
    );

    const openModal = (type) => {
        setModalOpen(true);
        setModalType(type);
        
    };

    const closeModal = () => {
        setModalOpen(false);
        setModalType(null);
    };

    const handleNext = (e) => {
        e.preventDefault();
        if(activate) {
            navigate("/createuser")
        };
    };

    return(
    <div className="Consent">
    <div className="consent_title">
        <h1>약관 동의가 필요해요.</h1>
    </div>
    <div className="all_choice">
    <label>
    <input type="checkbox" className="full_choice" checked={checkBox.length === 3 ? true:false} onChange={checkAll}/>
    <p>약관 전체동의<span>선택항목 포함</span></p>
    </label>
    </div>
    <div className="consent_item">
    <label><input type="checkbox" name="check_1" checked={checkBox.includes("check_1") ? true:false} onChange={handleCheck}/>
    <p>(필수) 이용약관</p></label>
    <button className="tos_btn" onClick={() => openModal("tos")}><GoArrowRight/></button>
    </div>
    <div className="consent_item">
    <label><input type="checkbox" name="check_2" checked={checkBox.includes("check_2") ? true:false} onChange={handleCheck}/>
    <p>(필수) 개인정보 수집 이용동의</p></label>
    <button className="tos_btn" onClick={() => openModal("TosCollection")}><GoArrowRight/></button>
    </div>
    
    <div className="consent_item">
    <label>
    <input type="checkbox" name="check_3" checked={checkBox.includes("check_3") ? true:false} onChange={handleCheck}/>
    <p>(선택) 개인정보 수집 이용동의</p></label>
    <button className="tos_btn" onClick={() => openModal("TosChoice")}><GoArrowRight/></button>
    </div>
    <Modal
        isOpen={modalOpen}
        onRequestClose={closeModal}
        ariaHideApp={false}
        className="modal"
        overlayClassName="modal_overlay">
            <button className="modal_btn" onClick={closeModal}>X</button>
            {modalType === "tos" && <Tos/>}
            {modalType === "TosCollection" && <TosCollection/>}
            {modalType === "TosChoice" && <TosChoice/>}
    </Modal>
    <div className="consent_btn"> 
        <button className="next_btn" disabled={!activate} onClick={handleNext}>다음</button>
    </div>
    </div>
    )
}

export default Consent;