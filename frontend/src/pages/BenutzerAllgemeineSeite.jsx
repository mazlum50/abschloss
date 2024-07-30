import React, { useContext, useState, useEffect } from 'react';
import '../styles/benutzerSeite.css';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import UserContext from '../UserContext.jsx';
import AusflugCard from '../components/AusflugCard.jsx';
import Button from '../utils/Button.jsx'; // Import Button component

const BenutzerAllgemeineSeite = () => {
    const { benutzer, benutzerHandle } = useContext(UserContext);
    const location = useLocation();
    const navigate = useNavigate();
    const { userId } = location.state;
    const [currentUser, setCurrentUser] = useState(null); // Initialize as null
    const baseUrl = "http://localhost:8080/api";
    console.log(userId);

    useEffect(() => {
        axios.get(`${baseUrl}/benutzer/${userId}`)
            .then((res) => {
                setCurrentUser(res.data);
                console.log(res.data);
            })
            .catch((err) => {
                console.error("Error fetching all user's data:", err);
            });
    }, [userId]);

    useEffect(() => {
        axios.get(`${baseUrl}/benutzer/${benutzer.id}`)
            .then((res) => {
                benutzerHandle(res.data);
                console.log(res.data);
            })
            .catch((err) => {
                console.error("Error fetching all user's data:", err);
            });
    }, [benutzer.id, benutzerHandle]); // Correct dependency array

    const handleNavigate = () => {
        navigate('/ausflug-erstellen');
    };

    const benutzerAusfluege = benutzer ? benutzer.ausfluege : [];
    console.log(benutzerAusfluege);
    const userAusfluege = currentUser ? currentUser.ausfluege : [];
    console.log(userAusfluege);
    const gemeinsameAusfluege = benutzerAusfluege && userAusfluege
        ? benutzerAusfluege.filter((benutzerAusflug) => userAusfluege.some((userAusflug) => benutzerAusflug.id === userAusflug.id))
        : [];
    console.log(gemeinsameAusfluege);

    return (
        <div className="position-relative z-index home min-h-screen flex flex-col items-center justify-center text-center">
            <div className="z-index container mx-auto px-4 flex flex-col items-center">
                {currentUser && (
                    <>
                        <div className="profile-section">
                            <img src="https://cdn.pixabay.com/photo/2015/03/04/22/35/avatar-659651_1280.png" alt="User" className="profile-image" />
                            <h2>{currentUser.userName}</h2>
                        </div>
                        <div className="about-section">
                            <h3>Über mich</h3>
                            <p className="ueber-mich">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Tempor nec feugiat nisl pretium fusce id velit ut. Dui faucibus in ornare.</p>
                        </div>
                    </>
                )}
                <h3>Gemeinsame Ausflüge</h3>
                {gemeinsameAusfluege.length > 0 ? (
                    gemeinsameAusfluege.map((ausflug, index) => (
                        <div className="cards-container" key={index}>
                            <div className="card-list">
                                <AusflugCard
                                    ausflug={ausflug}
                                    baseUrl={baseUrl}
                                />
                            </div>
                        </div>
                    ))
                ) : (
                    <div className="keine-ausfluege">
                        <p>Es gibt noch keine gemeinsame Ausfluege</p>
                        <Button
                            text="Ausflug erstellen"
                            onClick={handleNavigate}
                            color="var(--main-lavender-color)"
                        />
                    </div>
                )}
            </div>
        </div>
    );
};

export default BenutzerAllgemeineSeite;
