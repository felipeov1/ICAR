import React from 'react';
import { 
  FaFacebook, 
  FaTwitter, 
  FaInstagram, 
  FaLinkedin 
} from 'react-icons/fa';

const socialLinks = [
  { icon: FaFacebook, href: '#' },
  { icon: FaTwitter, href: '#' },
  { icon: FaInstagram, href: '#' },
  { icon: FaLinkedin, href: '#' }
];

const SocialLinks = () => {
  return (
    <div className="flex space-x-4">
      {socialLinks.map((social, index) => {
        const Icon = social.icon;
        return (
          <a
            key={index}
            href={social.href}
            className="text-gray-400 hover:text-white transition-colors"
          >
            <Icon size={24} />
          </a>
        );
      })}
    </div>
  );
};

export default SocialLinks;