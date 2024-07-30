import React, { useContext } from 'react';
import axios from 'axios';
import UserContext from "../UserContext.jsx";

const EinladungsMenu = ({ invitations }) => {
    const { benutzer, benutzerHandle, setInvitations } = useContext(UserContext);
    const baseUrl = "http://localhost:8080/api";

    const handleInvitation = (invitationId, accept) => {
        const url = `${baseUrl}/einladungen/${invitationId}/${accept ? 'annehmen' : 'ablehnen'}`;
        const method = accept ? axios.put : axios.delete;

        method(url)
            .then(response => {
                if (accept) {
                    const { benutzer: updatedBenutzer, ausflug } = response.data;
                    benutzerHandle({
                        ...updatedBenutzer,
                        ausfluege: [...(benutzer.ausfluege || []), ausflug]
                    });
                } else {
                    const updatedBenutzer = response.data;
                    benutzerHandle(updatedBenutzer);
                }
                // Remove the invitation from local state
                setInvitations(prevInvitations =>
                    prevInvitations.filter(inv => inv.id !== invitationId)
                );
            })
            .catch(error => {
                console.error('Error processing invitation:', error);
            });
    };

    return (
        <div className="absolute right-0 mt-2 w-48 bg-white rounded-md overflow-hidden shadow-xl z-10">
            {invitations.map(invitation => (
                <div key={invitation.id} className="px-4 py-2 border-b">
                    <p className="text-sm">{invitation.nameDesAusflugs}</p>
                    <div className="mt-2 flex justify-between">
                        <button
                            onClick={() => handleInvitation(invitation.id, true)}
                            className="bg-green-500 hover:bg-green-700 text-white text-xs font-bold py-1 px-2 rounded"
                        >
                            Annehmen
                        </button>
                        <button
                            onClick={() => handleInvitation(invitation.id, false)}
                            className="bg-red-500 hover:bg-red-700 text-white text-xs font-bold py-1 px-2 rounded"
                        >
                            Ablehnen
                        </button>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default EinladungsMenu;
