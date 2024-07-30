import React, { useContext, useState, useEffect } from 'react';
import '../styles/ausflugCard.css';
import { useNavigate } from "react-router-dom";
import { SlCalender, SlLocationPin } from "react-icons/sl";
import UserContext from "../UserContext.jsx";
import Popup from "./Popup.jsx";

const AusflugCard = ({ ausflug, baseUrl }) => {
    const [bildSrc, setBildSrc] = useState('');
    const navigate = useNavigate();
    const { benutzer } = useContext(UserContext);
    const [showPopup, setShowPopup] = useState(false);

    const handlePopupConfirm = () => {
        setShowPopup(false);
        navigate(`/login`);
    };

    const handleClick = () => {
        if (benutzer) {
            navigate(`/ausflug-seite/${ausflug.id}`, { state: { ausflug } });
        } else {
            setShowPopup(true);
        }
    };

    useEffect(() => {
        const fetchBild = async () => {
            if (ausflug.hauptBild?.imageUrl) {
                console.log()
                setBildSrc(ausflug.hauptBild.imageUrl);
            } else {
                try {
                    const response = await fetch(`${baseUrl}/api/ausflugbild/ausflug/${ausflug.id}`);
                    if (response.ok) {
                        const blob = await response.blob();
                        const objectURL = URL.createObjectURL(blob);
                        setBildSrc(objectURL);
                    } else {
                        console.error("Error fetching image:", response.statusText);
                    }
                } catch (error) {
                    console.error('Error fetching image:', error);
                }
            }
        };

        fetchBild();
    }, [ausflug, baseUrl]);

    return (
        <div className="ausflug-card" onClick={handleClick}>
            <div className="ausflug-card-bild" style={{ backgroundImage: `url(${bildSrc})` }}></div>
            <div className="ausflug-card-content">
                <h2 className="ausflug-card-content-h2">{ausflug.titel}</h2>
                <div className="ausflug-card-info">
                    <SlCalender className="ausflug-card-icon" />
                    <span>{ausflug.ausflugsdatum}</span>
                </div>
                <div className="ausflug-card-info">
                    <SlLocationPin className="ausflug-card-icon" />
                    <span>{ausflug.reiseziel}</span>
                </div>
            </div>
            {showPopup && (
                <Popup
                    title="Bitte melde dich an, um den Ausflug anzusehen."
                    description=""
                    onCancel={() => setShowPopup(false)}
                    onConfirm={handlePopupConfirm}
                    cancelText="Schliessen"
                    confirmText="Anmelden"
                />
            )}
        </div>
    );
};

export default AusflugCard;
