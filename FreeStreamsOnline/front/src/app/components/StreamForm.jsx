import { useEffect, useState } from 'react';
import * as CategoryAPI from '../api/category';
import * as PlaylistAPI from '../api/playlist';
import * as API from '../api/stream';

export default function StreamForm({ id, onSuccess }) {
  const [name, setName] = useState('');
  const [image, setImage] = useState('');
  const [description, setDesc] = useState('');
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
      String(id);
      API.fetchStream(id).then((s) => {
        console.log('Fetching stream with id:', id);

        setName(s.name);
        setImage(s.image);
        setDesc(s.description);
        setPlaylist(String(s.playlistId));
        setCategory(String(s.categoryId));
      }).catch(error => {
        console.error('Error fetching stream:', error);
      });
    }
  }, [id]);

  async function handleSubmit(e) {
    e.preventDefault();
    const stream = { 
      name, 
      image, 
      description, 
      playlistId: String(playlistId), // Преобразуем в строку
      categoryId: String(categoryId)   // Преобразуем в строку
    };
    
    if (id) {
      await API.updateStream(String(id), stream);
    }
    else await API.createStream(stream);
    onSuccess();
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