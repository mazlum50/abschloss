import {useContext, useEffect, useState} from 'react';
import axios from 'axios';
import UserContext from "../UserContext.jsx";
import EinladungCard from "./EinladungCard.jsx";
import Button from "../utils/Button.jsx";

const EinladungenBenutzerSeite = () => {
    const {invitations, setInvitations, benutzer, benutzerHandle} = useContext(UserContext);
    const [loading, setLoading] = useState(false);
    const [ausfluege, setAusfluege] = useState([]);
    const [error, setError] = useState(null);
    const baseUrl = "http://localhost:8080/api";


    useEffect(() => {
        fetchAusfluege();
    }, []);

    const fetchAusfluege = async () => {
        try {
            const response = await fetch(`${baseUrl}/ausfluege`);
            if (!response.ok) {
                throw new Error('Anfrage schleeecht');
            }
            const data = await response.json();
            console.log(data)
            setAusfluege(data);
        } catch (error) {
            console.error('Error fetching Ausflüge:', error);
        }
    };

    const handleInvitation = (invitationId, accept) => {
        setLoading(true);
        setError(null);
        const invitation = invitations.find(inv => inv.id === invitationId);
        console.log(invitation)
        if (!invitation) {
            console.error('Invitation not found');
            setError('Einladung nicht gefunden');
            setLoading(false);
            return;
        }
        const url = `http://localhost:8080/api/einladungen/${invitationId}/${accept ? 'annehmen' : 'ablehnen'}`;
        const method = accept ? axios.put : axios.delete;

        method(url)
            .then(response => {
                if (accept) {
                    // Aktualisiere den Benutzer und seine Ausfluege
                    const { benutzer: updatedBenutzer, ausflug } = response.data;
                    if (invitation.art === 'EINLADUNG') {
                        // For EINLADUNG, add the new Ausflug to the user's list
                        benutzerHandle({
                            ...updatedBenutzer,
                            ausfluege: [...(benutzer.ausfluege || []), ausflug]
                        });
                    } else {
                        // For ANFRAGE, just update the user data
                        benutzerHandle(updatedBenutzer);
                    }
                } else {
                    // Aktualisiere nur den Benutzer
                    const updatedBenutzer = response.data;
                    benutzerHandle(updatedBenutzer);
                }
                // Entferne die Einladung aus dem lokalen Zustand
                setInvitations(prevInvitations =>
                    prevInvitations.filter(inv => inv.id !== invitationId)
                );
            })
            .catch(error => {
                console.error('Fehler beim Bearbeiten der Einladung:', error);
                setError('Ein Fehler ist aufgetreten. Bitte versuchen Sie es später erneut.');
            })
            .finally(() => setLoading(false));
    };

    const renderInvitation = (invitation) => {
        const isInvitation = invitation.art === 'EINLADUNG';

        const ausflug = ausfluege.find(a => a.id === invitation.ausflugId);
        const message = isInvitation
            ? `${invitation.nameDesTeilnehmers} lädt dich zum ${invitation.nameDesAusflugs} ein:`
            : `Anfrage zum Teilnehmen von ${invitation.nameDesTeilnehmers}`;


        return (
            <div className="ausflug-card" key={invitation.ausflugId}>

                <div className="einladungMessage">
                    <p className="mb-4">{message}</p>
                    <div className="flex space-x-4" id="einladung-btn">
                        <Button
                            text={isInvitation ? "Annehmen" : "Genehmigen"}
                            onClick={() => handleInvitation(invitation.id, true)}
                            color="var(--main-green-color)"
                        />
                        <Button
                            className=""
                            text={isInvitation ? "Ablehnen" : "Verweigern"}
                            onClick={() => handleInvitation(invitation.id, false)}
                            color="var(--main-lavender-color)"
                            icon={() => <span className="icon-cancel">✖</span>}
                        />
                    </div>
                </div>
                <EinladungCard ausflug={ausflug} baseUrl={baseUrl}/>
            </div>
        );
    };

    return (
        <div className="card-list">
            {error && <p className="text-red-500">{error}</p>}
            {invitations.length > 0 ? (
                invitations.map(invitation => renderInvitation(invitation))
            ) : ""}
        </div>
    );
};

export default EinladungenBenutzerSeite;
