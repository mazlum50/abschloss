import React, { useState } from 'react';
import axios from 'axios';

const BildHochladen = ({ ausflugId, maxFileSize, onImageUpload }) => {
  const [isFileUpload, setIsFileUpload] = useState(true);
  const [file, setFile] = useState(null);
  const [imageUrl, setImageUrl] = useState('');  // Initialize with an empty string
  const [errorMessage, setErrorMessage] = useState('');

  const handleToggle = () => {
    setIsFileUpload(!isFileUpload);
    setFile(null);
    setImageUrl('');
    setErrorMessage('');
  };

  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];
    if (selectedFile && selectedFile.size > maxFileSize) {
      setErrorMessage(`File size exceeds the limit of ${maxFileSize / 1024 / 1024} MB`);
      setFile(null);
    } else {
      setFile(selectedFile);
      setErrorMessage('');
    }
  };

  const handleUrlChange = (event) => {
    setImageUrl(event.target.value);
    setErrorMessage('');
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (isFileUpload) {
      if (file) {
        const formData = new FormData();
        formData.append('image', file);

        try {
          const response = await axios.post(`http://localhost:8080/api/ausflugbild/ausflug/${ausflugId}/upload`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });
          console.log('Image file uploaded:', response.data);
          onImageUpload(URL.createObjectURL(file));
          setFile(null);
        } catch (error) {
          console.error('Error uploading image file:', error);
          setErrorMessage('Error uploading image file');
          setFile(null);
        }
      } else {
        setErrorMessage('Please select a file to upload');
      }
    } else {
      if (imageUrl) {
        try {
          const response = await axios.post(`http://localhost:8080/api/ausflugbild/ausflug/${ausflugId}/url`, null, {
            params: { imageUrl }
          });
          console.log('Image URL uploaded:', response.data);
          onImageUpload(imageUrl);
          setImageUrl('');
        } catch (error) {
          console.error('Error uploading image URL:', error);
          setErrorMessage('Error uploading image URL');
        }
      } else {
        setErrorMessage('Please enter an image URL');
      }
    }
  };

  return (
      <div>
        <div>
          <button type="button" onClick={handleToggle}>
            {isFileUpload ? 'Switch to URL' : 'Switch to File Upload'}
          </button>
        </div>
        <form onSubmit={handleSubmit}>
          {isFileUpload ? (
              <div>
                <input
                    className="fotos-zum-auswaehlen"
                    type="file"
                    accept="image/*,video/*"
                    onChange={handleFileChange}
                />
              </div>
          ) : (
              <div>
                <input
                    type="text"
                    value={imageUrl}
                    onChange={handleUrlChange}
                    placeholder="Enter image URL"
                />
              </div>
          )}
          <button type="submit">Submit</button>
        </form>
        {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
      </div>
  );
};

export default BildHochladen;
