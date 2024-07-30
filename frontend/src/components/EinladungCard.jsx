import { useContext, useState, useEffect } from 'react';
import '../styles/ausflugCard.css';
import { useNavigate } from "react-router-dom";
import { SlCalender, SlLocationPin } from "react-icons/sl";
import UserContext from "../UserContext.jsx";
import axios from "axios";

const EinladungCard = ({ ausflug, baseUrl }) => {
    const [bildSrc, setBildSrc] = useState('');
    const navigate = useNavigate();
    const { benutzer } = useContext(UserContext);
    const [ausflugDetails, setAusflugDetails] = useState({});

    const handleClick = () => {
        if (benutzer && ausflugDetails) {
            console.log(ausflugDetails)
            const ausflug = ausflugDetails;
            navigate(`/ausflug-seite/${ausflug.id}`, { state: { ausflug } });
        } else {
            // Assuming there's a state or function to show a popup if the user is not logged in
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
    }, [ausflugDetails, baseUrl]);

    useEffect(() => {
        if (ausflug) {
            console.log(ausflug.id)
            axios.get(`${baseUrl}/ausfluege/${ausflug.id}`)
                .then((res) => {
                    setAusflugDetails(res.data);
                })
                .catch((err) => {
                    console.error("Error fetching user data:", err);
                });
        }
    }, [ausflug, baseUrl]);



    return (
        <div className="ausflug-card" onClick={handleClick}>
            <div className="ausflug-card-bild" style={{ backgroundImage: `url(${bildSrc})` }}></div>
            <div className="ausflug-card-content">
                <h2 className="ausflug-card-content-h2">{ausflugDetails.titel}</h2>
                <div className="ausflug-card-info">
                    <SlCalender className="ausflug-card-icon" />
                    <span>{ausflugDetails.ausflugsdatum}</span>
                </div>
                <div className="ausflug-card-info">
                    <SlLocationPin className="ausflug-card-icon" />
                    <span>{ausflugDetails.reiseziel}</span>
                </div>
            </div>
        </div>
    );
};

export default EinladungCard;
