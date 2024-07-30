import { useState, useEffect } from 'react';

const useProfilePicture = (userId, triggerUpdate) => {
    const [profilePicturePreview, setProfilePicturePreview] = useState(null);

    useEffect(() => {
        console.log("Fetching profile picture for user:", userId);

        const fetchProfilePicture = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/profilbild/benutzer/${userId}`);
                if (response.ok) {
                    const contentType = response.headers.get('content-type');
                    if (contentType && contentType.startsWith('image/')) {
                        const blob = await response.blob();
                        const objectURL = URL.createObjectURL(blob);
                        setProfilePicturePreview(objectURL);
                        return () => URL.revokeObjectURL(objectURL); // Clean up URL object
                    } else {
                        const imageUrl = await response.text();
                        setProfilePicturePreview(imageUrl);
                    }
                } else {
                    console.error('Error loading profile picture');
                }
            } catch (error) {
                console.error('Error fetching profile picture:', error);
            }
        };

        fetchProfilePicture();
    }, [userId, triggerUpdate]);

    return profilePicturePreview;
};

export default useProfilePicture;
