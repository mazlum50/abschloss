// UserContext.jsx
import React, { createContext, useState, useEffect } from "react";
import axios from "axios";

const UserContext = createContext();

export function UserProvider({ children }) {
    const [invitations, setInvitations] = useState([]);
    const [benutzer, setBenutzer] = useState(() => {
        const savedBenutzer = localStorage.getItem("benutzer");
        try {
            return savedBenutzer ? JSON.parse(savedBenutzer) : null;
        } catch (e) {
            console.error("Error parsing saved benutzer from localStorage:", e);
            return null;
        }
    });


    useEffect(() => {
        if (benutzer) {
            localStorage.setItem("benutzer", JSON.stringify(benutzer));
            setInvitations(benutzer.einladungenSimpel || []);
        } else {
            localStorage.removeItem("benutzer");
            setInvitations([]);
        }
    }, [benutzer]);

    const benutzerHandle = (newBenutzer) => {
        setBenutzer(newBenutzer);
    };

    const fetchInvitations = () => {
        if (benutzer) {
            axios.get(`http://localhost:8080/api/einladungen/benutzer?benutzerId=${benutzer.id}`)
                .then(response => {
                    setInvitations(response.data);
                })
                .catch(error => {
                    console.error('Error fetching invitations:', error);
                });
        }
    };


    return (
        <UserContext.Provider value={{ benutzerHandle, benutzer, invitations, setInvitations, fetchInvitations }}>
            {children}
        </UserContext.Provider>
    );
}

export default UserContext;
