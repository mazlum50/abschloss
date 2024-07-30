import React, { useContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import UserContext from '../UserContext.jsx';
import Popup from '../components/Popup';
import ProfilePictureUpload from '../components/ProfilePictureUpload';
import '../styles/benutzerBearbeiten.css';

const BenutzerBearbeiten = () => {
    const { benutzer, benutzerHandle } = useContext(UserContext);
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        userName: benutzer.userName,
        userEmail: benutzer.userEmail,
        beschreibung: benutzer.beschreibung || ''
    });

    const [passwordData, setPasswordData] = useState({
        currentPassword: '',
        newPassword: '',
        verifyNewPassword: ''
    });

    const [showPopup, setShowPopup] = useState(false);
    const [showDeletePopup, setShowDeletePopup] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handlePasswordChange = (e) => {
        const { name, value } = e.target;
        setPasswordData({
            ...passwordData,
            [name]: value,
        });
    };

    const handleSubmit = async () => {
        const updatedUser = { ...formData };

        try {
            const response = await axios.put(`http://localhost:8080/api/benutzer/${benutzer.id}`, updatedUser);

            if (response.status === 200) {
                console.log('User profile updated:', response.data);
                benutzerHandle(response.data);
                navigate('/benutzer-seite');
            } else {
                console.error('Failed to update user profile');
            }
        } catch (error) {
            console.error('An error occurred:', error);
        }
    };

    const handlePasswordSubmit = async () => {
        if (passwordData.newPassword !== passwordData.verifyNewPassword) {
            console.log('New passwords do not match');
            return;
        }

        if (passwordData.newPassword.length < 8) {
            console.log('New password must be at least 8 characters long');
            return;
        }

        const passwordDataPayload = {
            currentPassword: passwordData.currentPassword,
            newPassword: passwordData.newPassword,
            newPasswordConfirm: passwordData.verifyNewPassword,
        };

        try {
            const response = await axios.put(`http://localhost:8080/api/benutzer/${benutzer.id}/passwort`, passwordDataPayload);

            if (response.status === 204) {
                console.log('Password updated successfully');
                benutzerHandle({ ...benutzer, password: passwordData.newPassword });
                navigate('/benutzer-seite');
            } else {
                console.error('Failed to update password');
            }
        } catch (error) {
            console.error('An error occurred:', error);
        }
    };

    const handleDeleteAccount = async () => {
        try {
            const response = await axios.delete(`http://localhost:8080/api/benutzer/${benutzer.id}`);

            if (response.status === 204) {
                console.log('User account deleted');
                navigate('/login');
            } else {
                console.error('Failed to delete user account');
            }
        } catch (error) {
            console.error('An error occurred:', error);
        }
    };

    const handleCancel = () => {
        navigate('/benutzer-seite');
    };

    const handlePopupConfirm = () => {
        setShowPopup(false);
        handleSubmit();
    };

    const handleDeletePopupConfirm = () => {
        setShowDeletePopup(false);
        handleDeleteAccount();
    };

    return (
        <div className="">
            <div className="">
                <div className="z-index rounded-lg p-8 w-full ausflugerstellen-max-w-md text-gray-700 mx-auto">
                    <ProfilePictureUpload benutzer={benutzer} benutzerHandle={benutzerHandle} />

                    <h1 className="title">Benutzer Bearbeiten</h1>
                    <form onSubmit={(e) => {
                        e.preventDefault();
                        setShowPopup(true);
                    }}>
                        <div className="input-group">
                            <label htmlFor="userName">Benutzername</label>
                            <input
                                type="text"
                                id="userName"
                                name="userName"
                                value={formData.userName}
                                onChange={handleChange}
                                placeholder="Benutzername eingeben"
                            />
                        </div>
                        <div className="input-group">
                            <label htmlFor="userEmail">E-Mail</label>
                            <input
                                type="email"
                                id="userEmail"
                                name="userEmail"
                                value={formData.userEmail}
                                onChange={handleChange}
                                placeholder="E-Mail eingeben"
                            />
                        </div>
                        <div className="input-group">
                            <label htmlFor="beschreibung">Beschreibung</label>
                            <textarea
                                id="beschreibung"
                                name="beschreibung"
                                value={formData.beschreibung}
                                onChange={handleChange}
                                placeholder="Beschreibung eingeben"
                                rows="3"
                            ></textarea>
                        </div>

                        <div className="button-group">
                            <button
                                type="button"
                                onClick={handleCancel}
                                className="button cancel-button"
                            >
                                Abbrechen
                            </button>
                            <button
                                type="submit"
                                className="button save-button"
                            >
                                Speichern
                            </button>
                        </div>
                    </form>

                    <h2 className="title">Change password</h2>
                    <form onSubmit={(e) => {
                        e.preventDefault();
                        handlePasswordSubmit();
                    }}>
                        <div className="input-group">
                            <label htmlFor="currentPassword">Current password</label>
                            <input
                                type="password"
                                id="currentPassword"
                                name="currentPassword"
                                value={passwordData.currentPassword}
                                onChange={handlePasswordChange}
                                placeholder="Enter your current password"
                            />
                        </div>
                        <div className="input-group">
                            <label htmlFor="newPassword">New password</label>
                            <input
                                type="password"
                                id="newPassword"
                                name="newPassword"
                                value={passwordData.newPassword}
                                onChange={handlePasswordChange}
                                placeholder="At least 8 characters"
                            />
                        </div>
                        <div className="input-group">
                            <label htmlFor="verifyNewPassword">Verify new password</label>
                            <input
                                type="password"
                                id="verifyNewPassword"
                                name="verifyNewPassword"
                                value={passwordData.verifyNewPassword}
                                onChange={handlePasswordChange}
                                placeholder="Re-enter the same new password"
                            />
                        </div>
                        <div className="button-group">
                            <button
                                type="button"
                                onClick={handleCancel}
                                className="button cancel-button"
                            >
                                abbrechen
                            </button>
                            <button
                                type="submit"
                                className="button save-button"
                            >
                                Speichern
                            </button>
                        </div>
                    </form>

                    <button
                        type="button"
                        className="button delete-button"
                        onClick={() => setShowDeletePopup(true)}
                    >
                        Konto löschen
                    </button>
                </div>
            </div>

            {showPopup && (
                <Popup
                    title="Änderungen speichern"
                    description="Möchten Sie die Änderungen wirklich speichern?"
                    onCancel={() => setShowPopup(false)}
                    onConfirm={handlePopupConfirm}
                    cancelText="Abbrechen"
                    confirmText="Speichern"
                />
            )}
            {showDeletePopup && (
                <Popup
                    title="Konto löschen"
                    description="Möchten Sie Ihr Konto wirklich löschen? Diese Aktion kann nicht rückgängig gemacht werden."
                    onCancel={() => setShowDeletePopup(false)}
                    onConfirm={handleDeletePopupConfirm}
                    cancelText="Abbrechen"
                    confirmText="Löschen"
                />
            )}
        </div>
    );
};

export default BenutzerBearbeiten;
