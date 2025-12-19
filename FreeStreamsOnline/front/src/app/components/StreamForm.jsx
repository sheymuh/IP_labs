import { useEffect, useState } from 'react';
import * as CategoryAPI from '../api/category';
import * as PlaylistAPI from '../api/playlist';
import * as API from '../api/stream';

export default function StreamForm({ id, onSuccess }) {
  const [name, setName] = useState('');
  const [image, setImage] = useState('');
  const [description, setDesc] = useState('');
  const [views, setViews] = useState('');
  const [publicationDate, setPublicationDate] = useState('');
  const [playlistId, setPlaylist] = useState('');
  const [categoryId, setCategory] = useState('');
  const [playlists, setPlaylists] = useState([]);
  const [categories, setCategories] = useState([]);

  console.log('StreamForm - id:', id);
    useEffect(() => {
        console.log('useEffect triggered, id:', id);

        CategoryAPI.fetchCategories().then(setCategories);
        PlaylistAPI.fetchPlaylists().then(setPlaylists);

        if (id) {
            const streamId = String(id);
            console.log('Fetching stream with id:', streamId);
            
            API.fetchStream(streamId)
                .then((s) => {
                    console.log('Fetched stream:', s);
                    
                    setName(s.name || '');
                    setImage(s.image || '');
                    setDesc(s.description || '');
                    setViews(s.views || 0);  // Добавить
                    setPublicationDate(s.publicationDate || new Date().toISOString().split('T')[0]); // Добавить
                    setPlaylist(String(s.playlist?.id || ''));
                    
                    // Берем первую категорию из массива
                    if (s.categories && s.categories.length > 0) {
                        setCategory(String(s.categories[0].id));
                    } else {
                        setCategory('');
                    }
                })
                .catch(error => {
                    console.error('Error fetching stream:', error);
                    alert('Не удалось загрузить стрим для редактирования');
                });
        }
    }, [id]);
    
    async function handleSubmit(e) {
        e.preventDefault();
        
        // Правильная структура согласно StreamRq.java
        const stream = { 
            name, 
            image, 
            description, 
            views: 0,  // Добавляем views (обязательное поле)
            publication_date: new Date().toISOString().split('T')[0], // Текущая дата
            playlistId: String(playlistId), 
            categoryIds: categoryId ? [Number(categoryId)] : []  // Массив ID категорий
        };
        
        console.log("Submitting stream data:", JSON.stringify(stream, null, 2));
        
        try {
            if (id) {
                await API.updateStream(String(id), stream);
            }
            else await API.createStream(stream);
            onSuccess();
        } catch (error) {
            console.error("Error submitting stream:", error);
            alert("Ошибка при сохранении: " + error.message);
        }
    }

  function handleFileChange(e) {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onloadend = () => {
      // @ts-ignore
      setImage(reader.result);
    };
    reader.readAsDataURL(file);
  }

  return (
    <form onSubmit={handleSubmit} className="container py-4">
      <h2>{id ? 'Редактировать' : 'Добавить'} трансляцию</h2>
      <p></p>
      
      <div className="mb-3">
        <label className="form-label">Название </label>
        <input
          type="text"
          className="form-control"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
      </div>

      <div className="mb-3">
        <label className="form-label">Описание </label>
        <textarea
          className="form-control"
          value={description}
          onChange={(e) => setDesc(e.target.value)}
          required
        ></textarea>
      </div>

        <div className="mb-3">
            <label className="form-label">Просмотры </label>
            <input
                type="number"
                className="form-control"
                value={views}
                onChange={(e) => setViews(e.target.value)}
                required
                min="0"
            />
        </div>

        <div className="mb-3">
            <label className="form-label">Дата публикации </label>
            <input
                type="date"
                className="form-control"
                value={publicationDate}
                onChange={(e) => setPublicationDate(e.target.value)}
                required
            />
        </div>

      <div className="mb-3">
        <label className="form-label">Изображение (файл) </label>
        <input
            type="file"
            className="form-control"
            onChange={handleFileChange}
            accept="image/*"
        />
      </div>
      {image && (
        <div className="mb-3">
            <label className="form-label">Предпросмотр:</label><br />
            <img src={image} alt="preview" className="img-thumbnail" style={{ maxHeight: "200px" }} />
        </div>
      )}

      <div className="mb-3">
        <label className="form-label">Категория </label>
        <select
          className="form-select"
          value={categoryId}
          // @ts-ignore
          onChange={(e) => setCategory(e.target.value)}
          required
        >
          <option value="">-- Выберите категорию --</option>
          {categories.map((с) => (
            <option key={с.id} value={String(с.id)}>
              {с.name}
            </option>
          ))}
        </select>
      </div>

      <div className="mb-3">
        <label className="form-label">Плейлист </label>
        <select
          className="form-select"
          value={playlistId}
          // @ts-ignore
          onChange={(e) => setPlaylist(e.target.value)}
          required
        >
          <option value="">-- Выберите плейлист --</option>
          {playlists.map((p) => (
            <option key={p.id} value={String(p.id)}>
              {p.name}
            </option>
          ))}
        </select>
      </div>

      <button type="submit" className="btn btn-primary">
        {id ? 'Сохранить' : 'Создать'}
      </button>
    </form>
  );
}