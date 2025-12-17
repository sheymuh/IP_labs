import { Link } from 'react-router-dom';

export const Header = () => {
    return (
        <header className="d-flex align-items-center justify-content-between position-sticky h-60 p-3">
            <div className="header-logo d-flex align-items-center g-3">
                <Link to="/">
                    <img src="/КАЙФ.jpg" alt="Логотип" />
                </Link>
                <h1>СТРИМЫ ОНЛАЙН БЕСПЛАТНО</h1>
            </div>
            <nav className="navbar d-flex align-items-center justify-content-center me-3 g-3 column-gap-2">
                <Link to="/category">Категории</Link>
                <div className="dropdown position-relative">
                    <span>
                        <a>Мой аккаунт ▾</a>
                    </span>
                    <div className="features-menu">
                        <div className="features-item">
                            <Link to="/account">Настройки</Link>
                        </div>
                        <div className="features-item">
                            <Link to="/subscriptions">Подписки</Link>
                        </div>
                        <div className="features-item">
                            <Link to="/savedStreams">Сохраненные трансляции</Link>
                        </div>
                    </div>
                </div>
            </nav>
        </header>
    );
};