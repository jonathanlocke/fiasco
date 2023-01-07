package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.network.core.EmailAddress;

import java.net.URL;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.string.CaseFormat.capitalize;
import static com.telenav.kivakit.network.core.EmailAddress.parseEmailAddress;
import static com.telenav.kivakit.resource.Urls.url;
import static digital.fiasco.runtime.build.metadata.MailingList.MailingListType.ANNOUNCEMENTS;
import static digital.fiasco.runtime.build.metadata.MailingList.MailingListType.COMMITS;
import static digital.fiasco.runtime.build.metadata.MailingList.MailingListType.DEVELOPER;
import static digital.fiasco.runtime.build.metadata.MailingList.MailingListType.ISSUES;
import static digital.fiasco.runtime.build.metadata.MailingList.MailingListType.NOTIFIACTIONS;
import static digital.fiasco.runtime.build.metadata.MailingList.MailingListType.USER;

public record MailingList(@FormatProperty String name,
                          @FormatProperty String description,
                          @FormatProperty MailingListType type,
                          @FormatProperty EmailAddress subscribeEmail,
                          @FormatProperty EmailAddress unsubscribeEmail,
                          @FormatProperty EmailAddress postEmail,
                          @FormatProperty URL archiveUrl,
                          @FormatProperty URL subscriptionManagementUrl,
                          @FormatProperty URL administrationUrl
)
{
    public static MailingList announcementsMailingList()
    {
        return mailingList(ANNOUNCEMENTS);
    }

    public static MailingList commitsMailingList()
    {
        return mailingList(COMMITS);
    }

    public static MailingList developerMailingList()
    {
        return mailingList(DEVELOPER);
    }

    public static MailingList issuesMailingList()
    {
        return mailingList(ISSUES);
    }

    public static MailingList mailingList(String name, MailingListType type)
    {
        return new MailingList(name, null, type, null, null, null, null, null, null);
    }

    public static MailingList mailingList(MailingListType type)
    {
        return new MailingList(capitalize(type.name()), null, type, null, null, null, null, null, null);
    }

    public static MailingList notificaitonsMailingList()
    {
        return mailingList(NOTIFIACTIONS);
    }

    public static MailingList userMailingList()
    {
        return mailingList("User", USER);
    }

    public enum MailingListType
    {
        USER,
        DEVELOPER,
        ISSUES,
        COMMITS,
        ANNOUNCEMENTS,
        NOTIFIACTIONS,
        OTHER
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public MailingList withAdministration(URL administration)
    {
        return new MailingList(name, description, type, subscribeEmail, unsubscribeEmail, postEmail, archiveUrl, subscriptionManagementUrl, administration);
    }

    public MailingList withAdministration(String administration)
    {
        return withAdministration(url(administration));
    }

    public MailingList withArchive(URL archive)
    {
        return new MailingList(name, description, type, subscribeEmail, unsubscribeEmail, postEmail, archive, subscriptionManagementUrl, administrationUrl);
    }

    public MailingList withArchive(String archive)
    {
        return withArchive(url(archive));
    }

    public MailingList withDescription(String description)
    {
        return new MailingList(name, description, type, subscribeEmail, unsubscribeEmail, postEmail, archiveUrl, subscriptionManagementUrl, administrationUrl);
    }

    public MailingList withName(String name)
    {
        return new MailingList(name, description, type, subscribeEmail, unsubscribeEmail, postEmail, archiveUrl, subscriptionManagementUrl, administrationUrl);
    }

    public MailingList withPost(EmailAddress post)
    {
        return new MailingList(name, description, type, subscribeEmail, unsubscribeEmail, post, archiveUrl, subscriptionManagementUrl, administrationUrl);
    }

    public MailingList withPost(String post)
    {
        return withPost(parseEmailAddress(throwingListener(), post));
    }

    public MailingList withSubscribe(EmailAddress subscribe)
    {
        return new MailingList(name, description, type, subscribe, unsubscribeEmail, postEmail, archiveUrl, subscriptionManagementUrl, administrationUrl);
    }

    public MailingList withSubscribe(String subscribe)
    {
        return withSubscribe(parseEmailAddress(throwingListener(), subscribe));
    }

    public MailingList withSubscriptionManagement(URL subscriptionManagement)
    {
        return new MailingList(name, description, type, subscribeEmail, unsubscribeEmail, postEmail, archiveUrl, subscriptionManagement, administrationUrl);
    }

    public MailingList withSubscriptionManagement(String subscriptionManagement)
    {
        return withSubscriptionManagement(url(subscriptionManagement));
    }

    public MailingList withType(MailingListType type)
    {
        return new MailingList(name, description, type, subscribeEmail, unsubscribeEmail, postEmail, archiveUrl, subscriptionManagementUrl, administrationUrl);
    }

    public MailingList withUnsubscribe(EmailAddress unsubscribe)
    {
        return new MailingList(name, description, type, subscribeEmail, unsubscribe, postEmail, archiveUrl, subscriptionManagementUrl, administrationUrl);
    }

    public MailingList withUnsubscribe(String unsubscribe)
    {
        return withUnsubscribe(parseEmailAddress(throwingListener(), unsubscribe));
    }
}
