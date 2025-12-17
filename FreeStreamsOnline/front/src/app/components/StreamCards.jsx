export const StreamCards = ({ stream, onEdit, onDelete }) => {
    return (
         <div className="card stream-card" style={{ maxWidth: '100%' }}>
            <div className="d-flex justify-content-center align-items-center p-3">
                <img 
                    src={stream.image} 
                    className="card-img-top w-100" 
                    alt={stream.name} 
                    style={{ 
                        height: '250px',
                        objectFit: 'cover'
                    }}
                />
            </div>
            <div className="card-body d-flex flex-column">
                <h5 className="card-title">Название: {stream.name}</h5>
                <h6 className="card-subtitle text-muted">Описание: {stream.description}</h6>
                <p className="card-text">Плейлист: {stream.playlist?.name || "Не найден"}</p>
                <p className="card-text">Категория: <em>{stream.category?.name || "Не найден"}</em></p>
                <div className="card-buttons">
                    <button className="btn btn-warning" onClick={onEdit}>Редактировать</button>
                    <button className="btn btn-danger" onClick={onDelete}>Удалить</button>
                </div>
            </div>
        </div>
    );
}