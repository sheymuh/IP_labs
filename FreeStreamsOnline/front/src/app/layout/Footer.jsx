export const Footer = () => {
    return (
        <footer className="footer d-flex justify-content-center align-items-center p-3 position-relative">
            <div className="company-name text-center">
                RBCS CORP. {new Date().getFullYear()} <i className="bi bi-c-circle"></i>
            </div>
            <div className="footer-icons d-flex gap-3 position-absolute end-0 me-3">
                <a href="https://vk.com/sheym_not_shame" target="_blank" rel="noreferrer">
                    <img src="/vk_icon.png" alt="vk" />
                </a>
                <a href="https://t.me/sheymuh" target="_blank" rel="noreferrer">
                    <img src="/tg_icon.png" alt="tg" />
                </a>
            </div>
        </footer>
    );
};