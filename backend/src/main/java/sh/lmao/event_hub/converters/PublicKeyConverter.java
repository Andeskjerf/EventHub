package sh.lmao.event_hub.converters;

import java.io.ByteArrayInputStream;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class PublicKeyConverter implements Converter<String, RSAPublicKey> {
    @Override
    public RSAPublicKey convert(String from) {
        return RsaKeyConverters.x509().convert(new ByteArrayInputStream(from.getBytes()));
    }
}
